package snownee.cuisine.data.adapter;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.util.JSONUtils;
import snownee.cuisine.api.RecipeRule;
import snownee.cuisine.data.CuisineDataManager;
import snownee.cuisine.impl.rule.FalseRecipeRule;
import snownee.cuisine.impl.rule.NotRecipeRule;

public class RecipeRuleAdapter implements JsonDeserializer<RecipeRule> {

    @Override
    public RecipeRule deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject o = json.getAsJsonObject();
        String type = JSONUtils.getString(o, "type", "");
        boolean not = !type.isEmpty() && type.charAt(0) == '!';
        if (not) {
            type = type.substring(1);
        }
        JsonDeserializer<RecipeRule> deserializer = CuisineDataManager.ruleAdapters.get(type);
        RecipeRule rule;
        if (deserializer == null) {
            rule = FalseRecipeRule.INSTANCE;
        } else {
            rule = deserializer.deserialize(json, typeOfT, context);
        }
        return not ? new NotRecipeRule(rule) : rule;
    }

}
