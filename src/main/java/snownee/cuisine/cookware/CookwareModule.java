package snownee.cuisine.cookware;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntityType;
import snownee.cuisine.api.registry.Cookware;
import snownee.cuisine.cookware.block.OvenBlock;
import snownee.cuisine.cookware.tile.OvenTile;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.KiwiModule;
import snownee.kiwi.Name;

@KiwiModule(name = "cookware", dependencies = "@core")
@KiwiModule.Optional
@KiwiModule.Group("decorations")
public final class CookwareModule extends AbstractModule {

    public static final OvenBlock OVEN = new OvenBlock(blockProp(Material.IRON));
    @Name("oven")
    public static final Cookware OVEN_TYPE = new Cookware(OVEN);
    @Name("oven")
    public static final TileEntityType<OvenTile> OVEN_TILE = TileEntityType.Builder.create(OvenTile::new, OVEN).build(null);

    public static final OvenBlock SAUCEPAN = new OvenBlock(blockProp(Material.IRON));

    public static final OvenBlock DRINKRO = new OvenBlock(blockProp(Material.IRON));

    public static final OvenBlock BOWL = new OvenBlock(blockProp(Material.WOOD));
    @Name("bowl")
    public static final Cookware BOWL_TYPE = new Cookware(BOWL);

}
