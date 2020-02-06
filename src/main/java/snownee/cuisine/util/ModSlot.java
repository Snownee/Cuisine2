package snownee.cuisine.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

//TODO move to Kiwi
public class ModSlot extends Slot {

    public ModSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        System.out.println(inventory.isItemValidForSlot(slotNumber, stack));
        return inventory.isItemValidForSlot(slotNumber, stack);
    }

}
