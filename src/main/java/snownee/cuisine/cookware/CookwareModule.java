package snownee.cuisine.cookware;

import net.minecraft.block.material.Material;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import snownee.cuisine.cookware.block.OvenBlock;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.KiwiModule;
import snownee.kiwi.Name;

@KiwiModule(name = "cookware", dependencies = "@core")
@KiwiModule.Optional
@KiwiModule.Group("decorations")
public final class CookwareModule extends AbstractModule {

    public static final OvenBlock OVEN = new OvenBlock(blockProp(Material.IRON));

    public static final OvenBlock SAUCEPAN = new OvenBlock(blockProp(Material.IRON));

    public static final OvenBlock DRINKRO = new OvenBlock(blockProp(Material.IRON));


}
