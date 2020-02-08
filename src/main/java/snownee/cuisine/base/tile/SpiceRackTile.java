package snownee.cuisine.base.tile;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import snownee.cuisine.api.tile.KitchenTile;
import snownee.cuisine.base.BaseModule;

public class SpiceRackTile extends KitchenTile {

    protected LazyOptional<SpiceHandler> itemHandler = LazyOptional.of(() -> new SpiceHandler(6));

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

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void remove() {
        super.remove();
        itemHandler.invalidate();
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("Inv", itemHandler.orElse(null).serializeNBT());
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        itemHandler.orElse(null).deserializeNBT(compound.getCompound("Inv"));
    }

}
