package snownee.cuisine.cookware.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import snownee.cuisine.cookware.CookwareModule;
import snownee.cuisine.cookware.tile.CookwareTile;
import snownee.cuisine.util.ModSlot;

public class BowlContainer extends CookwareContainer {

    public BowlContainer(int id, PlayerInventory playerInventory) {
        super(CookwareModule.BOWL_TYPE, id, playerInventory, CookwareModule.BOWL_TILE.create().getInventory());
    }

    public BowlContainer(int id, PlayerInventory playerInventory, CookwareTile tile) {
        super(CookwareModule.BOWL_TYPE, id, playerInventory, tile);
    }

    @Override
    protected int addInputSlots(IInventory inventory) {
        assertInventorySize(inventory, 4 + 3);
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                this.addSlot(new ModSlot(inventory, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }
        return 3;
    }

}
