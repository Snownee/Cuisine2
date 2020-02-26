package snownee.cuisine.cookware.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IntReferenceHolder;
import snownee.cuisine.api.LogicalServerSide;
import snownee.cuisine.api.registry.Cookware;
import snownee.cuisine.base.item.RecipeItem;
import snownee.cuisine.cookware.network.SUpdateSpicesPacket;
import snownee.cuisine.cookware.tile.AbstractCookwareTile;
import snownee.cuisine.cookware.tile.CookwareTile;
import snownee.cuisine.data.RecordData;
import snownee.cuisine.util.ModSlot;

public class CookwareContainer extends Container {

    @LogicalServerSide
    protected AbstractCookwareTile tile;
    public final PlayerEntity player;
    protected int cookTime;
    protected boolean cycle;
    protected Cookware cookware;

    public CookwareContainer(Cookware cookware, int id, PlayerInventory playerInventory) {
        this(cookware, id, playerInventory, cookware.getTileType().create().getInventory());
    }

    @LogicalServerSide
    public CookwareContainer(Cookware cookware, int id, PlayerInventory playerInventory, CookwareTile tile) {
        this(cookware, id, playerInventory, tile.getInventory());
        this.tile = tile;
        new SUpdateSpicesPacket(tile.getSpiceHandler().getSpices()).send((ServerPlayerEntity) playerInventory.player);
    }

    public CookwareContainer(Cookware cookware, int id, PlayerInventory playerInventory, IInventory inventory) {
        super(cookware.getContainer(), id);
        this.cookware = cookware;
        this.player = playerInventory.player;
        addSlots(playerInventory, inventory);
    }

    protected void addSlots(PlayerInventory playerInventory, IInventory inventory) {
        int slot = addInputSlots(inventory);

        this.addSlot(new ModSlot(inventory, ++slot, 124, 35) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });
        this.addSlot(new ModSlot(inventory, ++slot, 114, 5));
        this.addSlot(new ModSlot(inventory, ++slot, 134, 5));

        for (int k = 0; k < 3; ++k) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for (int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
        }

        trackInt(new IntReferenceHolder() {
            @Override
            public void set(int value) {
                cookTime = value;
            }

            @Override
            public int get() {
                return cookTime;
            }
        });
    }

    protected int addInputSlots(IInventory inventory) {
        assertInventorySize(inventory, 12);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlot(new ModSlot(inventory, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }
        return 8;
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

    public void startCooking(boolean cycle) {
        if (tile == null || tile.isRemoved() || !tile.canCook() || !cycle && tile.isOutputFull()) {
            return;
        }
        if (tile.getCookingPlayer() != null && tile.getCookingPlayer() != player) {
            return;
        }
        tile.startCooking(this);
        this.cycle = cycle;
        cookTime = cookware.getCookingTime();
    }

    public void tick() {
        if (cookTime == 0) {
            return;
        }
        if (--cookTime == 0) {
            updateProgressBar(0, cookTime);
            ItemStack recipe = tile.getRecipeHandler().getStackInSlot(0);
            if (!recipe.isEmpty()) {
                RecordData data = RecipeItem.getData(recipe);
                if (data != null && data.getCookware() == tile.getCookware()) {
                    //TODO
                }
            }
            if (tile.cookAsItem(player, cycle) && cycle) {
                startCooking(true);
            } else {
                tile.stopCooking();
            }
        }
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        if (tile != null) {
            tile.stopCooking();
        }
    }

    public int getCookProgressionScaled() {
        int j = cookware.getCookingTime();
        int i = j - cookTime;
        return j != 0 && cookTime != 0 ? i * 24 / j : 0;
    }

}
