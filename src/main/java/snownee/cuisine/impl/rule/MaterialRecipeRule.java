package snownee.cuisine.impl.rule;

import java.lang.reflect.Type;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.tags.Tag;
import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.RecipeRule;
import snownee.cuisine.api.registry.Material;

public class MaterialRecipeRule implements RecipeRule {

    private ImmutableSet<Material> materials;
    private ImmutableSet<Tag<Material>> tags;
    private int min;

    public MaterialRecipeRule(Set<Material> materials, int min) {
        
    }

    @Override
    public boolean apply(FoodBuilder input) {
        // TODO Auto-generated method stub
        return false;
    }

    public static class Adapter implements JsonDeserializer<MaterialRecipeRule> {

        @Override
        public MaterialRecipeRule deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            // TODO Auto-generated method stub
            return null;
        }

    }
}
