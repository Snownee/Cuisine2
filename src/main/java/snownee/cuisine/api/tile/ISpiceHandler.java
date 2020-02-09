package snownee.cuisine.api.tile;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public interface ISpiceHandler extends IItemHandler {

    public void addSubHandler(ItemStackHandler handler);

    public void removeSubHandler(ItemStackHandler handler);
}
