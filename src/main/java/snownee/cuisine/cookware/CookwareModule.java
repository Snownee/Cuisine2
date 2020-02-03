package snownee.cuisine.cookware;

import net.minecraft.block.material.Material;
import snownee.cuisine.cookware.block.OvenBlock;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.KiwiModule;

@KiwiModule(name = "cookware", dependencies = "@core")
@KiwiModule.Optional
@KiwiModule.Group("decorations")
public final class CookwareModule extends AbstractModule {

    public static final OvenBlock OVEN = new OvenBlock(blockProp(Material.IRON));

    public static final OvenBlock SAUCEPAN = new OvenBlock(blockProp(Material.IRON));

    public static final OvenBlock DRINKRO = new OvenBlock(blockProp(Material.IRON));

}
