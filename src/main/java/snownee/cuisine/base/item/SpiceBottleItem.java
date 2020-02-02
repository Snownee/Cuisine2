package snownee.cuisine.base.item;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import snownee.cuisine.CuisineConst;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.CuisineRegistries;
import snownee.cuisine.api.registry.Spice;
import snownee.kiwi.item.ModItem;
import snownee.kiwi.util.NBTHelper;
import snownee.kiwi.util.Util;

public class SpiceBottleItem extends ModItem {

    public final int maxVolume;

    public static final int VOLUME_PER_ITEM = 10;
    public static final int FLUID_PER_VOLUME = 100;

    public static final String SPICE = "spice";
    public static final String SPICE_VALUE = "spice.value";
    public static final String SPICE_NAME = "spice.name";

    public SpiceBottleItem(int maxVolume, Properties builder) {
        super(builder);
        this.maxVolume = maxVolume;
    }

    /**
     * @param container 被放的东西
     * @param in        要放的东西
     * @return 能装多少volume
     */
    public int fill(ItemStack container, ItemStack in, IFluidHandler.FluidAction action) {
        Spice spiceIn = CuisineAPI.findSpice(in).orElse(null);
        if (spiceIn == null) {
            return 0;
        }
        if (!getSpice(container).map(spiceIn::equals).orElse(true)) {
            return 0;
        }
        NBTHelper nbt = NBTHelper.of(container);
        int volume = nbt.getInt(SPICE_VALUE);
        int fill_item = (maxVolume - volume) / VOLUME_PER_ITEM;
        fill_item = Math.min(fill_item, in.getCount());
        volume += fill_item * VOLUME_PER_ITEM;
        if (action.execute()) {
            nbt.setString(SPICE_NAME, spiceIn.getRegistryName().toString());
            nbt.setInt(SPICE_VALUE, volume);
            in.shrink(fill_item);
        }
        return fill_item * VOLUME_PER_ITEM;
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
            return isFluidValid(0, fluid);
        }

        @Override
        public boolean canDrainFluidType(FluidStack fluid) {
            return isFluidValid(0, fluid);
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack fluid) {
            return !fluid.getFluid().getAttributes().isGaseous(fluid) && fluid.getFluid().getAttributes().getTemperature(fluid) < 400 && super.canFillFluidType(fluid);
        }

        @Override
        public void setContainerToEmpty() {
            container.removeChildTag(FLUID_NBT_KEY);
            container.removeChildTag(SPICE);
        }

        @Override
        public int fill(FluidStack resource, FluidAction doFill) {
            int ret = super.fill(resource, doFill);
            if (doFill.execute() && ret > 0) {
                updateSpice();
            }
            return ret;
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            FluidStack ret = super.drain(maxDrain, action);
            if (action.execute() && !ret.isEmpty()) {
                updateSpice();
            }
            return ret;
        }

        //FIXME:CHECK ME
        private void updateSpice() {
            FluidStack fl = this.getFluid();
            NBTHelper nbt = NBTHelper.of(getContainer());
            Optional<Spice> spice = CuisineAPI.findSpice(fl);
            spice.ifPresent(i->{
                nbt.setString(SPICE_NAME,i.getRegistryName().toString());
                nbt.setInt(SPICE_VALUE,fl.getAmount()/FLUID_PER_VOLUME);
            });
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack container, @Nullable CompoundNBT nbt) {
        return new SpiceFluidHandler(container, FLUID_PER_VOLUME * maxVolume);
    }

    public boolean hasFluid(ItemStack container) {
        return FluidUtil.getFluidContained(container).map(FluidStack::isEmpty).orElse(false);
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

    //FIXME add FluidAction (simulate)
    public boolean consume(ItemStack container, int amount) {
        if (amount <= VOLUME_PER_ITEM && amount > 0) {
            if (hasFluid(container)) {
                LazyOptional<IFluidHandlerItem> handlerOp = FluidUtil.getFluidHandler(container);
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

    @Override
    @Nonnull
    public UseAction getUseAction(ItemStack stack) {
        if (hasFluid(stack)) {
            LazyOptional<IFluidHandlerItem> handler = FluidUtil.getFluidHandler(stack);
            if (handler.isPresent()) {
                FluidStack fluidStack = handler.orElse(null).drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
                return fluidStack.getAmount() == FluidAttributes.BUCKET_VOLUME ? UseAction.DRINK : UseAction.NONE;
            }
        } else if (hasSpice(stack) && getDurability(stack) == VOLUME_PER_ITEM) {
            return UseAction.DRINK;
        }
        return UseAction.NONE;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
        ItemStack held = playerIn.getHeldItem(handIn);
        if (!isContainerEmpty(held)) {
            playerIn.setActiveHand(handIn);
            return (getUseAction(held) == UseAction.NONE ? ActionResult.func_226250_c_(held) : ActionResult.func_226249_b_(held));
        }

        return ActionResult.func_226251_d_(held);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
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

    @Override
    @Nonnull
    public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
        NBTHelper nbt = NBTHelper.of(stack);
        String name = nbt.getString(SPICE_NAME);
        if (name == null)
            return new TranslationTextComponent(this.getTranslationKey(stack));
        else
            return new TranslationTextComponent(String.format("spice.%s", name.replace(":", ".")));
    }

    public int getLeft(ItemStack stack) {
        NBTHelper nbt = NBTHelper.of(stack);
        int num = nbt.getInt(SPICE_VALUE);
        if (num == 0) {
            nbt.remove(SPICE);
        }
        return num;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (isContainerEmpty(stack)) {
            return;
        }
        tooltip.add(new TranslationTextComponent(CuisineConst.REST).appendText(String.format(":%d/%d", getLeft(stack), maxVolume)));
    }
}
