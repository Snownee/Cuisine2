package snownee.cuisine.base.tile;

import net.minecraft.nbt.CompoundNBT;
import snownee.cuisine.api.tile.KitchenTile;
import snownee.cuisine.base.BaseModule;

public class SpiceRackTile extends KitchenTile {

    public SpiceRackTile() {
        super(BaseModule.SPICE_RACK_TILE);
    }

    @Override
    protected void readPacketData(CompoundNBT data) {
        // TODO Auto-generated method stub
        super.readPacketData(data);
    }

    @Override
    protected CompoundNBT writePacketData(CompoundNBT data) {
        // TODO Auto-generated method stub
        return super.writePacketData(data);
    }

}
