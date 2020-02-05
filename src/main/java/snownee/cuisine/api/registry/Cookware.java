package snownee.cuisine.api.registry;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class Cookware extends ForgeRegistryEntry<Cookware> {

    private final ImmutableSet<Block> blocks;

    public Cookware(Block... blocks) {
        this.blocks = ImmutableSet.copyOf(blocks);
    }

    public Set<Block> getBlocks() {
        return blocks;
    }

    @Override
    public String toString() {
        return "Cookware{" + getRegistryName() + "}";
    }

}
