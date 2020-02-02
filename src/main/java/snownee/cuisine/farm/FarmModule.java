package snownee.cuisine.farm;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import snownee.cuisine.base.block.SpiceRackBlock;
import snownee.cuisine.base.crafting.SpiceBottleFillingRecipe;
import snownee.cuisine.base.item.SpiceBottleItem;
import snownee.cuisine.base.tile.SpiceRackTile;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.KiwiModule;
import snownee.kiwi.Name;
import snownee.kiwi.RenderLayer;

import java.util.Set;

@KiwiModule(name = "farm", dependencies = "@core")
@KiwiModule.Group("decorations")
@KiwiModule.Optional
public class FarmModule extends AbstractModule {

    public static final CuisineCropsBlock PEANUT = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock CHILI = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock SCALLION = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock SPINACH = new CuisineCropsBlock(blockProp(Blocks.POTATOES));

}
