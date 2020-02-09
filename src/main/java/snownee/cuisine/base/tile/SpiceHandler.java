package snownee.cuisine.base.tile;

import java.util.Optional;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.registry.Spice;
import snownee.cuisine.base.item.SpiceBottleItem;

public class SpiceHandler implements IItemHandler, INBTSerializable<CompoundNBT> {
    private final ItemStackHandler handler;
    private final NonNullList<ItemStack> stacks;

    public SpiceHandler(int size) {
        handler = new ItemStackHandler(stacks = NonNullList.withSize(size, ItemStack.EMPTY)) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return stack.getItem() instanceof SpiceBottleItem;
            }
        };
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return stack.getItem() instanceof SpiceBottleItem || CuisineAPI.findSpice(stack).isPresent();
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        Optional<Spice> spice = CuisineAPI.findSpice(stack);
        if (simulate) {
            stack = stack.copy();
        }
        if (spice.isPresent()) {
            for (ItemStack container : stacks) {
                if (!(container.getItem() instanceof SpiceBottleItem)) {
                    continue;
                }
                SpiceBottleItem bottle = (SpiceBottleItem) container.getItem();
                stack = bottle.fill(container, stack, simulate);
                // 正常情况下此处应当执行onContentsChanged
            }
            return stack;
        }
        if (slot == stacks.size()) {
            return stack;
        }
        return handler.insertItem(slot, stack, simulate);
    }

    @Override
    public int getSlots() {
        return stacks.size() + 1;
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        if (slot == stacks.size()) {
            return ItemStack.EMPTY;
        }
        return handler.getStackInSlot(slot);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot == stacks.size()) {
            return ItemStack.EMPTY;
        }
        return handler.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return handler.getSlotLimit(slot);
    }

    public ItemStackHandler getHandler() {
        return handler;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return handler.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        handler.deserializeNBT(nbt);
    }
}
