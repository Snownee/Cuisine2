package snownee.cuisine.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.CuisineRegistries;
import snownee.cuisine.api.registry.Spice;
import snownee.kiwi.item.ModItem;
import snownee.kiwi.util.NBTHelper;
import snownee.kiwi.util.Util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SpiceBottleItem extends ModItem {
    public SpiceBottleItem(Properties builder) {
        super(builder);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static final int VOLUME_PER_ITEM = 10;
    public static final int MAX_VOLUME_ITEM = 5;
    public static final String SPICE_VALUE = "spice.value";
    public static final String SPICE_NAME = "spice.name";
    public static final String SPICE = "spice";

    /**
     * @param container 被放的东西
     * @param in        要放的东西
     * @return 能装多少
     */
    public int fill(ItemStack container, ItemStack in, IFluidHandler.FluidAction action) {
        AtomicInteger num = new AtomicInteger();
        CuisineAPI.findSpice(in).ifPresent(spice -> {
            NBTHelper nbt = NBTHelper.of(container);
            if (nbt.getInt(SPICE_VALUE) == 0) {
                //获取能装多少
                int fill_item = Math.min(MAX_VOLUME_ITEM, in.getCount());
                if (action.execute()){
                    nbt.setString(SPICE_NAME, spice.getRegistryName().toString());
                    nbt.setInt(SPICE_VALUE, fill_item * VOLUME_PER_ITEM);
                }
                num.set(fill_item);
            } else if (Objects.equals(nbt.getString(SPICE_NAME), spice.getRegistryName().toString())) {
                //获取现在的余量
                int c = nbt.getInt(SPICE_VALUE);
                //如果没满
                if (c < MAX_VOLUME_ITEM * VOLUME_PER_ITEM) {
                    //获取还能装多少
                    int fill_count = (MAX_VOLUME_ITEM * VOLUME_PER_ITEM - c + 9) / VOLUME_PER_ITEM;
                    //获取转多少东西
                    int fill_item = Math.min(fill_count, in.getCount());
                    //获取装完的东西是多少
                    int now_num = Math.min(fill_item * VOLUME_PER_ITEM + c, MAX_VOLUME_ITEM * VOLUME_PER_ITEM);
                    if (action.execute())
                        nbt.setInt(SPICE_VALUE, now_num);
                    num.set(fill_item);
                }
            }
        });
        return num.get();
    }

    /**
     * @param container 被放的东西
     * @param in        要放的东西
     * @return 能装多少
     */
    public int fill(ItemStack container, FluidStack in, IFluidHandler.FluidAction action) {
        int num = fillFluid(container, in, action);
        if (num == 0) return 0;
        if (action.execute()) {
            CuisineAPI.findSpice(in).ifPresent(spice -> {
                NBTHelper nbt = NBTHelper.of(container);
                nbt.setString(SPICE_NAME, spice.getRegistryName().toString());
                nbt.setInt(SPICE_VALUE, num / FluidAttributes.BUCKET_VOLUME);
            });
        }
        return num;
    }

    private int fillFluid(ItemStack container, FluidStack in, IFluidHandler.FluidAction action) {
        AtomicInteger num = new AtomicInteger(0);
        container.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).ifPresent(i -> {
            if (i instanceof SpiceFluidHandler) {
                SpiceFluidHandler handler = ((SpiceFluidHandler) i);
                num.set(handler.fill(in, action));
            }
        });
        return num.get();
    }

    public Optional<Spice> getSpice(ItemStack container) {
        NBTHelper nbt = NBTHelper.of(container);
        return Optional.ofNullable(CuisineRegistries.SPICES.getValue(Util.RL(nbt.getString(SPICE_NAME))));
    }

    public boolean hasSpice(ItemStack container) {
        NBTHelper nbt = NBTHelper.of(container);
        if (nbt.getInt(SPICE_VALUE) == 0) {
            nbt.remove(SPICE);
            return false;
        }
        return true;
    }

    public static class SpiceFluidHandler extends FluidHandlerItemStack {
        public SpiceFluidHandler(ItemStack container, int capacity) {
            super(container, capacity);
        }

        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return !fluid.getFluid().getAttributes().isGaseous(fluid) &&
                    fluid.getFluid().getAttributes().getTemperature(fluid) < 400 &&
                    super.canFillFluidType(fluid);
        }

        @Override
        public void setContainerToEmpty() {
            super.setContainerToEmpty();
            container.setTag(null);
        }

    }

    public ICapabilityProvider initCapabilities(ItemStack container, @Nullable CompoundNBT nbt) {
        return new SpiceFluidHandler(container, MAX_VOLUME_ITEM * FluidAttributes.BUCKET_VOLUME);
    }

    public boolean hasFluid(ItemStack container) {
        if (!container.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).isPresent()) {
            return false;
        }
        AtomicBoolean flag = new AtomicBoolean(false);
        getFluidHandler(container).ifPresent(i -> {
            flag.set(i.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE).equals(FluidStack.EMPTY));
        });
        return flag.get();
    }

    public LazyOptional<IFluidHandlerItem> getFluidHandler(ItemStack container) {
        return container.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
    }

    public int getDurability(ItemStack container) {
        return NBTHelper.of(container).getInt(SPICE);
    }

    public void setDurability(ItemStack stack, int durability) {
        if (durability > 0) {
            NBTHelper.of(stack).setInt(SPICE_VALUE, durability);
            stack.setDamage(1);
        } else {
            stack.setTag(null);
            stack.setDamage(0);
        }
    }

    public boolean consume(ItemStack container, int amount) {
        if (amount <= VOLUME_PER_ITEM && amount > 0) {
            if (hasFluid(container)) {
                LazyOptional<IFluidHandlerItem> handlerOp = getFluidHandler(container);
                if (!handlerOp.isPresent()) {
                    return false;
                }
                int amountFluid = FluidAttributes.BUCKET_VOLUME * amount / VOLUME_PER_ITEM;
                IFluidHandlerItem handler = handlerOp.orElse(null);
                FluidStack fluidStack = handler.drain(amountFluid, IFluidHandler.FluidAction.SIMULATE);
                if (fluidStack != FluidStack.EMPTY && fluidStack.getAmount() == amountFluid) {
                    handler.drain(amountFluid, IFluidHandler.FluidAction.EXECUTE);
                    return true;
                }
            } else if (hasSpice(container)) {
                int volume = getDurability(container);
                if (volume >= amount) {
                    setDurability(container, volume - amount);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isContainerEmpty(ItemStack stack) {
        return !hasSpice(stack);
    }

    @Nonnull
    public UseAction getUseAction(ItemStack stack) {
        if (hasFluid(stack)) {
            LazyOptional<IFluidHandlerItem> handler = getFluidHandler(stack);
            if (handler.isPresent()) {
                FluidStack fluidStack = handler.orElse(null).drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
                return fluidStack.getAmount() == FluidAttributes.BUCKET_VOLUME ? UseAction.DRINK : UseAction.NONE;
            }
        } else if (hasSpice(stack) && getDurability(stack) == VOLUME_PER_ITEM) {
            return UseAction.DRINK;
        }
        return UseAction.NONE;
    }

    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
        ItemStack held = playerIn.getHeldItem(handIn);
        if (!isContainerEmpty(held)) {
            playerIn.setActiveHand(handIn);
            return (getUseAction(held) == UseAction.NONE ? ActionResult.func_226250_c_(held) : ActionResult.func_226249_b_(held));
        }

        return ActionResult.func_226251_d_(held);
    }

    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    public void fillItemGroup(@Nonnull ItemGroup group, @Nonnull NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            ItemStack stack = new ItemStack(this);
            items.add(stack.copy());
            CuisineRegistries.SPICES.getKeys().forEach(i -> {
                ItemStack s = stack.copy();
                NBTHelper nbt = NBTHelper.of(s);
                nbt.setString(SPICE_NAME, i.toString());
                items.add(s);
            });
        }
    }

    @Nonnull
    public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
        NBTHelper nbt = NBTHelper.of(stack);
        String name = nbt.getString(SPICE_NAME);
        if (name == null)
            return new TranslationTextComponent(this.getTranslationKey(stack));
        else
            return new TranslationTextComponent(String.format("spice.%s", name.replace(":", ".")));

    }
    public int getLeft(ItemStack stack){
        NBTHelper nbt = NBTHelper.of(stack);
        int num  = nbt.getInt(SPICE_VALUE);
        if (num==0){
            nbt.remove(SPICE);
        }
        return num;
    }
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (isContainerEmpty(stack)){
            return;
        }
        tooltip.add(new TranslationTextComponent("cuisine.rest")
                .appendText(String.format(":%d/%d",getLeft(stack),VOLUME_PER_ITEM*MAX_VOLUME_ITEM)));
    }
}
