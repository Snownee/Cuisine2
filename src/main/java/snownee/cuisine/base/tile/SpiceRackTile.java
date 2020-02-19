package snownee.cuisine.base.tile;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import snownee.cuisine.api.tile.KitchenTile;
import snownee.cuisine.base.BaseModule;
import snownee.cuisine.base.container.SpiceRackContainer;
import snownee.cuisine.base.item.SpiceBottleItem;
import snownee.cuisine.util.InvHandlerWrapper;

public class SpiceRackTile extends KitchenTile implements INamedContainerProvider {

    protected ItemStackHandler itemHandler = new ItemStackHandler(6) {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getItem() instanceof SpiceBottleItem;
        }
    };

    public SpiceRackTile() {
        super(BaseModule.SPICE_RACK_TILE);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return multiblock.getCap().cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("Inv", itemHandler.serializeNBT());
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        itemHandler.deserializeNBT(compound.getCompound("Inv"));
    }

    public IInventory getInventory() {
        return new InvHandlerWrapper(itemHandler) {
            @Override
            public void setInventorySlotContents(int index, ItemStack stack) {
                if (world != null && !world.isRemote) {
                    ItemStack before = getStackInSlot(index);
                    SpiceBottleItem.getSpice(before).ifPresent(spice -> {
                        getSpiceHandler().addSpice(spice, -SpiceBottleItem.getDurability(before));
                    });
                }
                super.setInventorySlotContents(index, stack);
                if (world != null && !world.isRemote) {
                    SpiceBottleItem.getSpice(stack).ifPresent(spice -> {
                        getSpiceHandler().addSpice(spice, SpiceBottleItem.getDurability(stack));
                    });
                }
            }
        };
    }

    @Override
    protected IItemHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
        return new SpiceRackContainer(id, playerInventory, this);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(Util.makeTranslationKey("container", getType().getRegistryName()));
    }

}
