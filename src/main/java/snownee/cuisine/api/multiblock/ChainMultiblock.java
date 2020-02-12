package snownee.cuisine.api.multiblock;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.event.TickEvent.Phase;
import snownee.cuisine.api.CuisineCapabilities;
import snownee.cuisine.base.block.SpiceRackBlock;
import snownee.kiwi.schedule.Scheduler;
import snownee.kiwi.schedule.impl.SimpleWorldTask;

public class ChainMultiblock<T> implements Supplier<T>, INBTSerializable<CompoundNBT> {

    public static final int MAX_BLOCKS = 5;

    protected TileEntity tile;
    @Nullable
    protected LazyOptional<T> tCap;
    @Nullable
    protected ChainMultiblock<T> master;
    @Nullable
    public HashMap<BlockPos, Supplier<ChainMultiblock<T>>> all;

    public ChainMultiblock(TileEntity tile, NonNullSupplier<T> ctxFactory, CompoundNBT compound) {
        this.tile = tile;
        if (compound == null) {
            init();
        } else {
            deserializeNBT(compound);
        }
        if (all == null) {
            tCap = LazyOptional.of(ctxFactory);
        }
    }

    private void init() {
        World world = tile.getWorld();
        BlockPos pos = tile.getPos();
        for (Direction direction : Direction.VALUES) {
            TileEntity neighbor = world.getTileEntity(pos.offset(direction));
            if (neighbor == null) {
                continue;
            }
            ChainMultiblock<T> multiblock = neighbor.getCapability(CuisineCapabilities.MULTIBLOCK).orElse(null);
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
                    multiblock.all.values().forEach(m -> m.get().setMaster(master));
                }
            }
        }
        if (master == null) {
            setMaster(this);
        }
    }

    public boolean setMaster(ChainMultiblock multiblock) {
        if (master == multiblock) {
            addSelf();
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
        addSelf();
        // for testing:
        if (tile.getWorld() != null) {
            BlockState state = tile.getWorld().getBlockState(tile.getPos());
            if (state.has(SpiceRackBlock.LIT)) {
                tile.getWorld().setBlockState(tile.getPos(), state.with(SpiceRackBlock.LIT, isMaster()));
            }
        }
        return true;
    }

    private void addSelf() {
        ChainMultiblock<T> master = getMaster();
        if (tile.hasWorld() && master.all != null && !master.all.containsKey(tile.getPos())) {
            master.all.put(tile.getPos(), () -> this);
        }
    }

    public boolean isMaster() {
        return master == null;
    }

    @Nonnull
    public ChainMultiblock<T> getMaster() {
        return isMaster() ? this : master;
    }

    public void remove() {
        if (getMaster().all != null && getMaster().all.size() > 1) {
            getMaster().all.remove(tile.getPos());
            Map<BlockPos, ChainMultiblock> map = Maps.newHashMap();
            getMaster().all.forEach((k, v) -> map.put(k, v.get()));
            Set<ChainMultiblock> origins = Sets.newLinkedHashSet();
            if (!isMaster()) {
                origins.add(master);
            }
            for (Direction direction : Direction.VALUES) {
                BlockPos pos = tile.getPos().offset(direction);
                if (map.containsKey(pos)) {
                    origins.add(map.get(pos));
                }
            }
            if (origins.size() > 1 || !origins.contains(master)) {
                Set<BlockPos> added = Sets.newHashSet();
                for (ChainMultiblock origin : origins) {
                    if (!added.contains(origin.tile.getPos())) {
                        origin.search(null, origin, map, added);
                    }
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
        for (Direction direction : Direction.VALUES) {
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
        CompoundNBT data = new CompoundNBT();
        if (isMaster()) {
            ListNBT list = new ListNBT();
            for (Entry<BlockPos, Supplier<ChainMultiblock<T>>> e : all.entrySet()) {
                CompoundNBT element = NBTUtil.writeBlockPos(e.getKey());
                list.add(element);
            }
            data.put("All", list);
        }
        return data;
    }

    @Override
    public void deserializeNBT(CompoundNBT data) {
        if (!data.contains("All", Constants.NBT.TAG_LIST)) {
            return;
        }
        setMaster(this);
        ListNBT list = data.getList("All", Constants.NBT.TAG_COMPOUND);
        for (INBT e : list) {
            CompoundNBT element = (CompoundNBT) e;
            BlockPos pos = NBTUtil.readBlockPos(element);
            Supplier<ChainMultiblock<T>> multiblock = Lazy.of(() -> {
                TileEntity tile = this.tile.getWorld().getTileEntity(pos);
                if (tile != null) {
                    return tile.getCapability(CuisineCapabilities.MULTIBLOCK).orElse(null);
                }
                return null;
            });
            all.put(pos, multiblock);
        }
    }

    public TileEntity getTile() {
        return tile;
    }

    public void onLoad() {
        if (all == null) {
            return;
        }
        addSelf();
        Scheduler.add(new SimpleWorldTask(tile.getWorld(), Phase.START, i -> {
            all.values().stream().forEach(m -> {
                if (m.get() != this) {
                    m.get().master = this;
                }
            });
            return true;
        }));
    }

    @Override
    public T get() {
        return getMaster().tCap.orElse(null);
    }

    public LazyOptional<T> getCap() {
        return getMaster().tCap;
    }

}
