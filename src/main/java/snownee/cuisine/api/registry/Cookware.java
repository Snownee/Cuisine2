package snownee.cuisine.api.registry;

import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ForgeRegistryEntry;
import snownee.cuisine.cookware.block.CookwareBlock;
import snownee.cuisine.cookware.tile.CookwareTile;

public class Cookware extends ForgeRegistryEntry<Cookware> {

    private ImmutableSet<Block> blocks;
    private int inputSlots = 9;
    private int cookingTime = 20;
    private ContainerType<?> container;
    private TileEntityType<? extends CookwareTile> tileType;

    public Cookware setBlocks(@Nullable TileEntityType<? extends CookwareTile> tileType, Block... blocks) {
        this.blocks = ImmutableSet.copyOf(blocks);
        this.tileType = tileType;
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

    public TileEntityType<? extends CookwareTile> getTileType() {
        return tileType;
    }

    public Cookware setInputSlots(int inputSlots) {
        this.inputSlots = inputSlots;
        return this;
    }

    public int getInputSlots() {
        return inputSlots;
    }

    public Cookware setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
        return this;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public Cookware setContainer(ContainerType<?> container) {
        this.container = container;
        return this;
    }

    public ContainerType<?> getContainer() {
        return container;
    }

    @Override
    public String toString() {
        return "Cookware{" + getRegistryName() + "}";
    }

}
