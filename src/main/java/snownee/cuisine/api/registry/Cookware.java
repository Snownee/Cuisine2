package snownee.cuisine.api.registry;

import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ForgeRegistryEntry;
import snownee.cuisine.cookware.block.CookwareBlock;

public class Cookware extends ForgeRegistryEntry<Cookware> {

    private ImmutableSet<Block> blocks;
    private int inputSlots = 9;

    public Cookware setBlocks(@Nullable TileEntityType<?> tileType, Block... blocks) {
        this.blocks = ImmutableSet.copyOf(blocks);
        if (tileType != null) {
            for (Block block : this.blocks) {
                if (block instanceof CookwareBlock) {
                    ((CookwareBlock) block).setTileType(tileType);
                }
            }
        }
        return this;
    }

    public Set<Block> getBlocks() {
        return blocks;
    }

    public int getInputSlots() {
        return inputSlots;
    }

    public Cookware setInputSlots(int inputSlots) {
        this.inputSlots = inputSlots;
        return this;
    }

    @Override
    public String toString() {
        return "Cookware{" + getRegistryName() + "}";
    }

}
