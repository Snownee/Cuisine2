package snownee.cuisine.cap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import snownee.cuisine.api.multiblock.ChainMultiblock;

public final class CuisineCapabilitiesInternal {
    private CuisineCapabilitiesInternal() {}

    public static void register() {
        CapabilityManager.INSTANCE.register(ChainMultiblock.class, new Capability.IStorage<ChainMultiblock>() {
            @Override
            public INBT writeNBT(Capability<ChainMultiblock> capability, ChainMultiblock instance, Direction side) {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<ChainMultiblock> capability, ChainMultiblock instance, Direction side, INBT nbt) {
                instance.deserializeNBT((CompoundNBT) nbt);
            }
        }, () -> null);
    }

}
