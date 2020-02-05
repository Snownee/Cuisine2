package snownee.cuisine.cookware.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import snownee.cuisine.cookware.CookwareModule;

public class OvenTile extends CookwareTile {

    public OvenTile() {
        super(CookwareModule.OVEN_TILE, CookwareModule.OVEN_TYPE);
    }

    @Override
    public NonNullList<ItemStack> getMaterialItems() {
        NonNullList<ItemStack> items = NonNullList.create();
        items.add(new ItemStack(Items.RED_MUSHROOM));
        items.add(new ItemStack(Items.BROWN_MUSHROOM));
        return items;
    }

}
