package snownee.cuisine.api.multiblock;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.tile.IMasterHandler;

public class KitchenMultiblock extends ChainMultiblock<IMasterHandler, IItemHandler> {

    public KitchenMultiblock(TileEntity tile, CompoundNBT compound, IItemHandler itemHandler) {
        super(tile, compound, itemHandler);
    }

    @Override
    protected IMasterHandler createNewHandler() {
        return CuisineAPI.newSpiceHandler();
    }

}
