package snownee.cuisine.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import snownee.cuisine.api.multiblock.ChainMultiblock;

public final class CuisineCapabilities {

    private CuisineCapabilities() {}

    @CapabilityInject(ChainMultiblock.class)
    public static Capability<ChainMultiblock> MULTIBLOCK = null;

}
