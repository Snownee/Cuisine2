package snownee.cuisine.processing.inventory;

import com.mojang.datafixers.util.Either;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
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
