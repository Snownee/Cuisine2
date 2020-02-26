package snownee.cuisine.api;

import java.util.Collections;
import java.util.List;

import it.unimi.dsi.fastutil.objects.AbstractObject2FloatMap.BasicEntry;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;

public interface Bonus {
    default void unlock(ServerPlayerEntity player) {}

    default List<BasicEntry<EffectInstance>> addEffects() {
        return Collections.EMPTY_LIST;
    }
}
