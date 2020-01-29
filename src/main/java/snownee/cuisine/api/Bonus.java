package snownee.cuisine.api;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;

public interface Bonus {
    default void unlock(ServerPlayerEntity player) {}

    default List<Pair<EffectInstance, Float>> addEffects() {
        return Collections.EMPTY_LIST;
    }
}
