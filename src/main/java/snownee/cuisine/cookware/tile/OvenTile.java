package snownee.cuisine.cookware.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import snownee.cuisine.cookware.CookwareModule;

public class OvenTile extends CookwareTile {

    public OvenTile() {
        super(CookwareModule.OVEN_TILE, CookwareModule.OVEN_TYPE);
    }

    @Override
    public NonNullList<ItemStack> getMaterialItems() {
        // TODO Auto-generated method stub
        return null;
    }

}
