package snownee.cuisine.data.adapter;

import java.lang.reflect.Type;

import com.google.common.collect.ImmutableListMultimap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import snownee.cuisine.api.Bonus;
import snownee.cuisine.data.CuisineDataManager;

public class StarsAdapter implements JsonDeserializer<ImmutableListMultimap<Integer, Bonus>> {

    @Override
    public ImmutableListMultimap<Integer, Bonus> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ImmutableListMultimap.Builder<Integer, Bonus> builder = ImmutableListMultimap.builder();
        json.getAsJsonObject().entrySet().forEach(entry -> {
            entry.getValue().getAsJsonObject().entrySet().forEach(entry2 -> {
                JsonDeserializer<Bonus> adapter = CuisineDataManager.bonusAdapters.get(entry2.getKey());
                Bonus bonus = adapter.deserialize(entry2.getValue(), Bonus.class, context);
                builder.put(Integer.valueOf(entry.getKey()), bonus);
            });
        });
        return builder.build();
    }

}
