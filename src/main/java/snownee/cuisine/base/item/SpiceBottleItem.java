package snownee.cuisine.base.item;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.CuisineConst;
import snownee.cuisine.api.CuisineRegistries;
import snownee.cuisine.api.registry.Spice;
import snownee.kiwi.item.ModItem;
import snownee.kiwi.util.NBTHelper;
import snownee.kiwi.util.Util;

public class SpiceBottleItem extends ModItem {

    public final int maxItemVolume;
    public final int fluidCapacity;

    public static final int VOLUME_PER_ITEM = 1;
    public static final int FLUID_PER_VOLUME = FluidAttributes.BUCKET_VOLUME / 10;

    public static final String SPICE_VALUE = "Spice.Value";
    public static final String SPICE_NAME = "Spice.Name";

    public SpiceBottleItem(int maxItemVolume, int fluidCapacity, Properties builder) {
        super(builder.maxStackSize(1));
        this.maxItemVolume = maxItemVolume;
        this.fluidCapacity = fluidCapacity;
    }

    /**
     * @param container 被放的东西
     * @param in        要放的东西
     * @return 剩余in
     */
    public ItemStack fill(ItemStack container, ItemStack in, boolean simulate) {
        Spice spiceIn = CuisineAPI.findSpice(in).orElse(null);
        if (spiceIn == null) {
            return in;
        }
        if (!getSpice(container).map(spiceIn::equals).orElse(true)) {
            return in;
        }
        NBTHelper nbt = NBTHelper.of(container);
        int volume = nbt.getInt(SPICE_VALUE);
        int fill_item = (maxItemVolume - volume) / VOLUME_PER_ITEM;
        fill_item = Math.min(fill_item, in.getCount());
        if (simulate) {
            in = in.copy();
        }
        in.shrink(fill_item);
        if (!simulate) {
            volume += fill_item * VOLUME_PER_ITEM;
            nbt.setString(SPICE_NAME, spiceIn.getRegistryName().toString());
            nbt.setInt(SPICE_VALUE, volume);
        }
        return in;
    }

    public int fill(ItemStack container, FluidStack in, IFluidHandler.FluidAction action) {
        Spice spiceIn = CuisineAPI.findSpice(in).orElse(null);
        if (spiceIn == null) {
            return 0;
        }
        if (!getSpice(container).map(spiceIn::equals).orElse(true)) {
            return 0;
        }
        AtomicInteger ret = new AtomicInteger();
        container.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(i -> {
            ret.set(i.fill(in, action));
            ((SpiceFluidHandler) i).updateSpice();
        });
        return ret.get();
    }

    public static Optional<Spice> getSpice(ItemStack container) {
        if (!(container.getItem() instanceof SpiceBottleItem)) {
            return Optional.empty();
        }
        NBTHelper nbt = NBTHelper.of(container);
        return Optional.ofNullable(CuisineRegistries.SPICES.getValue(Util.RL(nbt.getString(SPICE_NAME))));
    }

    public boolean hasSpice(ItemStack container) {
        NBTHelper nbt = NBTHelper.of(container);
        if (nbt.getInt(SPICE_VALUE) == 0) {
            nbt.remove(CuisineConst.SPICE);
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
            container.removeChildTag(CuisineConst.SPICE);
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
            Optional<Spice> spice = CuisineAPI.findSpice(fl);
            spice.ifPresent(i -> {
                NBTHelper nbt = NBTHelper.of(getContainer());
                nbt.setString(SPICE_NAME, i.getRegistryName().toString());
                nbt.setInt(SPICE_VALUE, fl.getAmount() / FLUID_PER_VOLUME);
            });
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack container, @Nullable CompoundNBT nbt) {
        return new SpiceFluidHandler(container, fluidCapacity);
    }

    public static boolean hasFluid(ItemStack container) {
        return FluidUtil.getFluidContained(container).map(FluidStack::isEmpty).orElse(false);
    }

    // why not use getDamage? because there are some OP mending machines
    public int getMaxDurability(ItemStack stack) {
        return hasFluid(stack) ? fluidCapacity / FLUID_PER_VOLUME : maxItemVolume;
    }

    public static int getDurability(ItemStack stack) {
        return NBTHelper.of(stack).getInt(SPICE_VALUE);
    }

    private static void setDurability(ItemStack stack, int durability) {
        if (durability > 0) {
            NBTHelper.of(stack).setInt(SPICE_VALUE, durability);
        } else {
            stack.setTag(null);
        }
    }

    public boolean consume(ItemStack container, int amount, IFluidHandler.FluidAction action) {
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
                    if (action.execute())
                        handler.drain(amountFluid, IFluidHandler.FluidAction.EXECUTE);
                    return true;
                }
            } else if (hasSpice(container)) {
                int volume = getDurability(container);
                if (volume >= amount) {
                    if (action.execute())
                        setDurability(container, volume - amount);
                    return true;
                }
            }
        }
        return false;
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
        if (hasSpice(held)) {
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
            NBTHelper nbt = NBTHelper.of(stack);
            CuisineRegistries.SPICES.getKeys().forEach(i -> {
                nbt.setString(SPICE_NAME, i.toString());
                //FIXME illegal item. spice value?
                //FIXME should use fill()?
                items.add(stack.copy());
            });
        }
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
        Optional<Spice> spice = getSpice(stack);
        if (spice.isPresent())
            return spice.get().getDisplayName();
        else
            return super.getDisplayName(stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (!hasSpice(stack)) {
            return;
        }
        //FIXME
        tooltip.add(new TranslationTextComponent("cuisine.spice_bottle.rest").appendText(String.format(":%d/%d", getDurability(stack), maxItemVolume)));
    }
}
