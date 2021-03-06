package snownee.cuisine.client;

import java.util.concurrent.ExecutionException;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import net.minecraftforge.registries.IRegistryDelegate;
import snownee.cuisine.api.registry.Spice;

public final class ColorLookup {
    private ColorLookup() {}

    private static final Cache<IRegistryDelegate<Spice>, Integer> SPICES = CacheBuilder.newBuilder().build();

    public static int get(Spice spice) {
        try {
            return SPICES.get(spice.delegate, () -> compute(spice));
        } catch (ExecutionException e) {
            return -1;
        }
    }

    private static int compute(Spice spice) {
        return -1;
    }

    public static void invalidateAll() {
        SPICES.invalidateAll();
    }

}
