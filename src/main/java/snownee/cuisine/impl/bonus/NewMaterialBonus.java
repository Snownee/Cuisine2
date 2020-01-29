package snownee.cuisine.impl.bonus;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import snownee.cuisine.api.Bonus;

public class NewMaterialBonus implements Bonus {
    public static class Adapter implements JsonDeserializer<NewMaterialBonus> {
        @Override
        public NewMaterialBonus deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new NewMaterialBonus();
        }
    }
}
