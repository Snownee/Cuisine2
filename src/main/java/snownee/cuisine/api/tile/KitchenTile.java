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

    protected LazyOptional<ChainMultiblock> multiblock;

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
            return LazyOptional.of(() -> multiblock).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        ChainMultiblock multiblock = new ChainMultiblock(this);
        this.multiblock = LazyOptional.of(() -> multiblock);
    }

    @Override
    public void remove() {
        super.remove();
        multiblock.ifPresent(ChainMultiblock::remove);
        multiblock.invalidate();
        multiblock = null;
    }

}
