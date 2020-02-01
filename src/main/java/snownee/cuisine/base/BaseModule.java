package snownee.cuisine.base;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
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

@KiwiModule(name = "base", dependencies = "@core")
@KiwiModule.Group("decorations")
@KiwiModule.Optional
public class BaseModule extends AbstractModule {
    public static final SpiceBottleItem SPICE_BOTTLE = new SpiceBottleItem(50, itemProp());

    public static final IRecipeSerializer<?> SPICE_BOTTLE_FILL = new SpiceBottleFillingRecipe.Serializer();

    public static final SpiceRackBlock SPICE_RACK = new SpiceRackBlock(blockProp(Material.WOOD));

    public static final Set<Block> SPICE_RACK_VALID_BLOCKS = Sets.newHashSet(SPICE_RACK);
    @Name("spice_rack")
    public static final TileEntityType<?> SPICE_RACK_TILE = new TileEntityType<>(SpiceRackTile::new, SPICE_RACK_VALID_BLOCKS, null);
}
