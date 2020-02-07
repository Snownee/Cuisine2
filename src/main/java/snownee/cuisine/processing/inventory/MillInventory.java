package snownee.cuisine.processing.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import snownee.kiwi.crafting.EmptyInventory;

public class MillInventory extends EmptyInventory {
    public ItemStack item;
    public FluidStack fluid;
    public MillInventory(ItemStack item,FluidStack fluid){
        this.item = item;
        this.fluid = fluid;
    }
}
