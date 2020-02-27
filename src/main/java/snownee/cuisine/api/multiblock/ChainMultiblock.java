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
import net.minecraftforge.event.TickEvent.Phase;
import snownee.cuisine.api.CuisineCapabilities;
import snownee.cuisine.api.tile.IMasterHandler;
import snownee.cuisine.base.block.SpiceRackBlock;
import snownee.kiwi.schedule.Scheduler;
import snownee.kiwi.schedule.impl.SimpleWorldTask;

public abstract class ChainMultiblock<T extends IMasterHandler, X> implements Supplier<T>, INBTSerializable<CompoundNBT> {

    protected TileEntity tile;
    @Nullable
    protected LazyOptional<T> handlerCap;
    @Nullable
    protected ChainMultiblock<T, X> master;
    @Nullable
    public HashMap<BlockPos, Supplier<ChainMultiblock<T, X>>> all;
    @Nullable
    public final X x;

    public ChainMultiblock(TileEntity tile, CompoundNBT compound, X x) {
        this.tile = tile;
        this.x = x;
        if (compound == null) {
            init();
        } else {
            deserializeNBT(compound);
        }
        if (all == null) {
            T hanlder = createNewHandler();
            if (compound == null) {
                hanlder.addMultiblock(this);
            }
            handlerCap = LazyOptional.of(() -> hanlder);
        }
    }

    public abstract int getMaxBlocks();

    private void init() {
        World world = tile.getWorld();
        BlockPos pos = tile.getPos();
        for (Direction direction : Direction.VALUES) {
            TileEntity neighbor = world.getTileEntity(pos.offset(direction));
            if (neighbor == null) {
                continue;
            }
            ChainMultiblock<T, X> multiblock = neighbor.getCapability(CuisineCapabilities.MULTIBLOCK).orElse(null);
            if (multiblock != null) {
                multiblock = multiblock.getMaster();
                if (master == null || multiblock == master) {
                    if (!setMaster(multiblock.getMaster())) {
                        return;
                    }
                } else {
                    if (master.all.size() + multiblock.all.size() > getMaxBlocks()) {
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
            handlerCap = LazyOptional.of(this::createNewHandler);
        } else {
            if (multiblock.all.size() >= getMaxBlocks()) {
                destory();
                return false;
            }
            all = null;
            master = multiblock;
            handlerCap = null;
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
        ChainMultiblock<T, X> master = getMaster();
        if (tile.hasWorld() && master.all != null && !master.all.containsKey(tile.getPos())) {
            master.all.put(tile.getPos(), () -> this);
            getCap().orElse(null).addMultiblock(this);
        }
    }

    public boolean isMaster() {
        return master == null;
    }

    @Nonnull
    public ChainMultiblock<T, X> getMaster() {
        return isMaster() ? this : master;
    }

    public void remove() {
        if (getMaster().all != null) {
            getMaster().all.remove(tile.getPos());
            if (!getMaster().all.isEmpty()) {
                getCap().ifPresent(handler -> handler.removeMultiblock(this));
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
        }
        if (handlerCap != null) {
            handlerCap.invalidate();
            handlerCap = null;
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
            for (Entry<BlockPos, Supplier<ChainMultiblock<T, X>>> e : all.entrySet()) {
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
            Supplier<ChainMultiblock<T, X>> multiblock = Lazy.of(() -> {
                if (!this.tile.getWorld().isBlockPresent(pos)) {
                    return null;
                }
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
            all.values().stream().forEach($ -> {
                ChainMultiblock<T, X> mb = $.get();
                if (mb != this) {
                    mb.master = this;
                }
                get().addMultiblock(mb);
            });
            return true;
        }));
    }

    protected abstract T createNewHandler();

    @Override
    public T get() {
        return getMaster().handlerCap.orElse(null);
    }

    public LazyOptional<T> getCap() {
        return getMaster().handlerCap;
    }

}
