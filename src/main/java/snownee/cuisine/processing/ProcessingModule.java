package snownee.cuisine.processing;

import net.minecraft.block.material.Material;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntityType;
import snownee.cuisine.processing.block.MillBlock;
import snownee.cuisine.processing.crafting.MillingRecipe;
import snownee.cuisine.processing.tile.MillTile;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.KiwiModule;
import snownee.kiwi.Name;

@KiwiModule(name = "processing")
@KiwiModule.Optional
@KiwiModule.Group("decorations")
public final class ProcessingModule extends AbstractModule {

    public static final MillBlock MILL = new MillBlock(blockProp(Material.ROCK));

    @Name("mill")
    public static final TileEntityType<MillTile> MILL_TILE = TileEntityType.Builder.create(() -> new MillTile(millType()), MILL).build(null);

    private static TileEntityType<MillTile> millType() {
        return MILL_TILE;
    }

    @Name("milling")
    public static final IRecipeSerializer<?> MILL_RECIPE = new MillingRecipe.Serializer();

    @Name("milling")
    public static final IRecipeType<MillingRecipe> MILL_RECIPE_TYPE = new IRecipeType() {};
}
