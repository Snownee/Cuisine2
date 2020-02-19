package snownee.cuisine.api.tile;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraftforge.items.IItemHandler;
import snownee.cuisine.api.multiblock.KitchenMultiblock;
import snownee.cuisine.api.registry.Spice;

public interface ISpiceHandler extends IMasterHandler<KitchenMultiblock>, IItemHandler {

    Object2IntMap<Spice> getSpices();

    boolean useSpices(Object2IntMap<Spice> spices);

    void addSpice(Spice spice, int incr);

}
