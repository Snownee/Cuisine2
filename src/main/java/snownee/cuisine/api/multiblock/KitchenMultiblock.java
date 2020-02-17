package snownee.cuisine.api.multiblock;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.tile.ISpiceHandler;

public class KitchenMultiblock extends ChainMultiblock<ISpiceHandler, IItemHandler> {

    public KitchenMultiblock(TileEntity tile, CompoundNBT compound, IItemHandler itemHandler) {
        super(tile, compound, itemHandler);
    }

    @Override
    protected ISpiceHandler createNewHandler() {
        return CuisineAPI.newSpiceHandler();
    }

}
