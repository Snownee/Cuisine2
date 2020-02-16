package snownee.cuisine.api.tile;

import snownee.cuisine.api.multiblock.ChainMultiblock;

public interface IMasterHandler<MB extends ChainMultiblock<?, ?>> {

    void addMultiblock(MB multiblock);

    void removeMultiblock(MB multiblock);
}
