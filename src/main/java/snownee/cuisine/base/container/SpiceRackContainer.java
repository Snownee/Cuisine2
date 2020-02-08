package snownee.cuisine.base.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import snownee.cuisine.base.BaseModule;
import snownee.cuisine.base.tile.SpiceRackTile;
import snownee.cuisine.util.ModSlot;

public class SpiceRackContainer extends Container {

    protected SpiceRackTile tile;
    protected final PlayerEntity player;

    public SpiceRackContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, BaseModule.SPICE_RACK_TILE.create().getInventory());
    }

    public SpiceRackContainer(int id, PlayerInventory playerInventory, SpiceRackTile tile) {
        this(id, playerInventory, tile.getInventory());
        this.tile = tile;
    }

    public SpiceRackContainer(int id, PlayerInventory playerInventory, IInventory inventory) {
        super(BaseModule.SPICE_RACK_CONTAINER, id);
        assertInventorySize(inventory, 6);
        this.player = playerInventory.player;

        int i = 51;

        for (int j = 0; j < 6; ++j) {
            this.addSlot(new ModSlot(inventory, j, 44 + j * 18, 20));
        }

        for (int l = 0; l < 3; ++l) {
            for (int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + l * 9 + 9, 8 + k * 18, l * 18 + 51));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 109));
        }
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            int size = this.inventorySlots.size() - playerIn.inventory.mainInventory.size();
            if (index < size) {
                if (!this.mergeItemStack(itemstack1, size, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, size, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return !tile.isRemoved() && !(player.getDistanceSq(tile.getPos().getX() + 0.5D, tile.getPos().getY() + 0.5D, tile.getPos().getZ() + 0.5D) > 64.0D);
    }

}
