package snownee.cuisine.impl.bonus;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.registries.ForgeRegistries;
import snownee.cuisine.api.Bonus;
import snownee.kiwi.util.Util;

public class EffectsBonus implements Bonus {
    @Expose
    private final ImmutableList<Pair<EffectInstance, Float>> effects;

    public EffectsBonus(ImmutableList<Pair<EffectInstance, Float>> effects) {
        this.effects = effects;
    }

    @Override
    public List<Pair<EffectInstance, Float>> addEffects() {
        return effects;
    }

    public static class Adapter implements JsonDeserializer<EffectsBonus> {
        @Override
        public EffectsBonus deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ImmutableList.Builder<Pair<EffectInstance, Float>> builder = ImmutableList.builder();
            json.getAsJsonObject().entrySet().forEach(entry -> {
                JsonObject objectIn = entry.getValue().getAsJsonObject();
                Effect effect = ForgeRegistries.POTIONS.getValue(Util.RL(entry.getKey()));
                EffectInstance effectInstance = new EffectInstance(effect, JSONUtils.getInt(objectIn, "duration"), JSONUtils.getInt(objectIn, "amplifier", 0));
                builder.add(Pair.of(effectInstance, JSONUtils.getFloat(objectIn, "possibility", 1)));
            });
            return new EffectsBonus(builder.build());
        }
    }
}
