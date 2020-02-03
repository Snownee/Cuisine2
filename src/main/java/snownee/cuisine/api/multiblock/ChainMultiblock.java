package snownee.cuisine.api.multiblock;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import snownee.cuisine.api.CuisineCapabilities;
import snownee.cuisine.base.block.SpiceRackBlock;

public class ChainMultiblock implements INBTSerializable<CompoundNBT> {

    public static final int MAX_BLOCKS = 5;

    protected TileEntity tile;
    @Nullable
    protected ChainMultiblock master;
    @Nullable
    public HashMap<BlockPos, ChainMultiblock> all;

    public ChainMultiblock(TileEntity tile) {
        this.tile = tile;
        World world = tile.getWorld();
        BlockPos pos = tile.getPos();
        for (Direction direction : Direction.values()) { //TODO replace with Direction.VALUES
            TileEntity neighbor = world.getTileEntity(pos.offset(direction));
            if (neighbor == null) {
                continue;
            }
            ChainMultiblock multiblock = neighbor.getCapability(CuisineCapabilities.MULTIBLOCK, direction.getOpposite()).orElse(null);
            if (multiblock != null) {
                multiblock = multiblock.getMaster();
                if (master == null || multiblock == master) {
                    if (!setMaster(multiblock.getMaster())) {
                        return;
                    }
                } else {
                    if (master.all.size() + multiblock.all.size() > MAX_BLOCKS) {
                        destory();
                        return;
                    }
                    multiblock.all.values().forEach(m -> m.setMaster(master));
                }
            }
        }
        if (master == null) {
            setMaster(this);
        }
    }

    public boolean setMaster(ChainMultiblock multiblock) {
        if (master == multiblock) {
            getMaster().all.put(tile.getPos(), this);
            return true;
        }
        if (multiblock == this) {
            all = Maps.newHashMap();
            master = null;
        } else {
            if (multiblock.all.size() >= MAX_BLOCKS) {
                destory();
                return false;
            }
            all = null;
            master = multiblock;
        }
        getMaster().all.put(tile.getPos(), this);
        // for testing:
        BlockState state = tile.getWorld().getBlockState(tile.getPos());
        tile.getWorld().setBlockState(tile.getPos(), state.with(SpiceRackBlock.LIT, isMaster()));
        return true;
    }

    public boolean isMaster() {
        return master == null;
    }

    @Nonnull
    public ChainMultiblock getMaster() {
        return isMaster() ? this : master;
    }

    public void remove() {
        if (getMaster().all != null && getMaster().all.size() > 1) {
            getMaster().all.remove(tile.getPos());
            Map<BlockPos, ChainMultiblock> map = ImmutableMap.copyOf(getMaster().all);
            Set<ChainMultiblock> origins = Sets.newLinkedHashSet();
            if (!isMaster()) {
                origins.add(master);
            }
            for (Direction direction : Direction.values()) {
                BlockPos pos = tile.getPos().offset(direction);
                if (map.containsKey(pos)) {
                    origins.add(map.get(pos));
                }
            }
            Set<BlockPos> added = Sets.newHashSet();
            for (ChainMultiblock origin : origins) {
                if (!added.contains(origin.tile.getPos())) {
                    origin.search(null, origin, map, added);
                }
            }
        }
        tile = null;
    }

    public void search(@Nullable Direction from, ChainMultiblock master, Map<BlockPos, ChainMultiblock> map, Set<BlockPos> added) {
        added.add(tile.getPos());
        if (this == master && this.isMaster()) {
            master.all.clear();
        }
        setMaster(master);
        for (Direction direction : Direction.values()) {
            if (from == direction) {
                continue;
            }
            BlockPos pos = tile.getPos().offset(direction);
            if (added.contains(pos) || !map.containsKey(pos)) {
                continue;
            }
            map.get(pos).search(direction.getOpposite(), master, map, added);
        }
    }

    public void destory() {
        TileEntity tile = this.tile;
        remove();
        if (!tile.isRemoved()) {
            tile.getWorld().destroyBlock(tile.getPos(), true);
        }
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

    public TileEntity getTile() {
        return tile;
    }

}
