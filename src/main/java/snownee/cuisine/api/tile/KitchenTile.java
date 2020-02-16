package snownee.cuisine.api.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import snownee.cuisine.api.CuisineCapabilities;
import snownee.cuisine.api.multiblock.KitchenMultiblock;
import snownee.kiwi.tile.TextureTile;
import snownee.kiwi.util.NBTHelper;

public abstract class KitchenTile extends TextureTile {

    protected KitchenMultiblock multiblock;
    protected LazyOptional<KitchenMultiblock> multiblockOptional;

    public KitchenTile(TileEntityType<?> tileEntityTypeIn, String... textureKeys) {
        super(tileEntityTypeIn, textureKeys);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return writePacketData(new CompoundNBT());
    }

    @Override
    public void read(CompoundNBT compound) {
        NBTHelper data = NBTHelper.of(compound);
        if (world == null) {
            multiblock = new KitchenMultiblock(this, data.getTag("Multiblock"), getItemHandler());
        }
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (multiblock != null) {
            compound.put("Multiblock", multiblock.serializeNBT());
        }
        return super.write(compound);
    }

    @Override
    protected void readPacketData(CompoundNBT data) {
        super.readPacketData(data);
    }

    @Override
    protected CompoundNBT writePacketData(CompoundNBT data) {
        return super.writePacketData(data);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (multiblock != null && cap == CuisineCapabilities.MULTIBLOCK) {
            return multiblockOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!world.isRemote) {
            if (multiblock == null) {
                multiblock = new KitchenMultiblock(this, null, getItemHandler());
            } else {
                multiblock.onLoad();
            }
        }
        if (multiblock != null) {
            multiblockOptional = LazyOptional.of(() -> multiblock);
        }
    }

    protected IItemHandler getItemHandler() {
        return null;
    }

    @Override
    public void remove() {
        super.remove();
        if (multiblock != null) {
            multiblock.remove();
        }
        if (multiblockOptional != null) {
            multiblockOptional.invalidate();
            multiblockOptional = null;
        }
    }

}
