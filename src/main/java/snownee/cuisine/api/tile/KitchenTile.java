package snownee.cuisine.api.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import snownee.cuisine.api.CuisineCapabilities;
import snownee.cuisine.api.multiblock.ChainMultiblock;
import snownee.kiwi.tile.TextureTile;

public abstract class KitchenTile extends TextureTile {

    protected ChainMultiblock multiblock;
    protected LazyOptional<ChainMultiblock> multiblockOptional = LazyOptional.of(() -> multiblock);

    public KitchenTile(TileEntityType<?> tileEntityTypeIn, String... textureKeys) {
        super(tileEntityTypeIn, textureKeys);
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
        if (cap == CuisineCapabilities.MULTIBLOCK) {
            return multiblockOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.multiblock = new ChainMultiblock(this);
    }

    @Override
    public void remove() {
        super.remove();
        multiblockOptional.invalidate();
        multiblockOptional = null;
        if (multiblock != null) {
            multiblock.remove();
        }
    }

}
