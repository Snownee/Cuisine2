package snownee.cuisine.api.multiblock;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import snownee.cuisine.api.CuisineCapabilities;

public class ChainMultiblock implements INBTSerializable<CompoundNBT> {

    @Nullable
    protected ChainMultiblock master;
    protected TileEntity tile;

    public ChainMultiblock(TileEntity tile) {
        this.tile = tile;
        World world = tile.getWorld();
        BlockPos pos = tile.getPos();
        for (Direction direction : Direction.values()) {
            TileEntity neighbor = world.getTileEntity(pos.offset(direction));
            if (neighbor == null) {
                continue;
            }
            LazyOptional<ChainMultiblock> result = neighbor.getCapability(CuisineCapabilities.MULTIBLOCK, direction.getOpposite());
            if (result.isPresent()) {
                master = result.orElse(null);
                master.addSlave(this);
                return;
            }
        }
        master = this;
    }

    public void addSlave(ChainMultiblock multiblock) {
        // TODO Auto-generated method stub

    }

    public void appointMaster() {

    }

    public boolean isMaster() {
        return master == null;
    }

    public ChainMultiblock getMaster() {
        return isMaster() ? this : master;
    }

    public void remove() {
        tile = null;
    }

    @Override
    public CompoundNBT serializeNBT() {
        // TODO Auto-generated method stub
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT data) {
        // TODO Auto-generated method stub

    }

}
