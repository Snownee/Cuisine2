package snownee.cuisine.impl.rule;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import snownee.cuisine.Cuisine;
import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.RecipeRule;
import snownee.kiwi.util.Util;

public class CountRegistryRecipeRule<T extends IForgeRegistryEntry<T>> implements RecipeRule {

    private ImmutableSet<T> materials;
    private int min;

    public CountRegistryRecipeRule(ImmutableSet<T> materials, int min) {
        this.materials = materials;
        this.min = min;
    }

    @Override
    public boolean test(FoodBuilder builder) {
        int count = 0;
        for (T material : materials) {
            count += builder.count(material);
            if (count >= min) {
                return true;
            }
        }
        return false;
    }

    public static class Adapter<T extends IForgeRegistryEntry<T>> implements JsonDeserializer<CountRegistryRecipeRule> {

        private final TagCollection<T> tagCollection;
        private final ForgeRegistry<T> registry;

        public Adapter(TagCollection<T> tagCollection, ForgeRegistry<T> registry) {
            this.tagCollection = tagCollection;
            this.registry = registry;
        }

        @Override
        public CountRegistryRecipeRule deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject o = json.getAsJsonObject();
            ImmutableSet.Builder<T> builder = ImmutableSet.builder();
            if (o.has("value")) {
                builder.addAll(parse(JSONUtils.getString(o, "value")));
            }
            if (o.has("values")) {
                for (JsonElement element : JSONUtils.getJsonArray(o, "values")) {
                    builder.addAll(parse(element.getAsString()));
                }
            }
            return new CountRegistryRecipeRule(builder.build(), JSONUtils.getInt(o, "min", 1));
        }

        private Collection<T> parse(String key) {
            if (key.isEmpty()) {
                return Collections.EMPTY_LIST;
            }
            if (key.charAt(0) == '#') {
                Tag<T> tag = tagCollection.get(Util.RL(key.substring(1), Cuisine.MODID));
                return tag.getAllElements();
            }
            T go = registry.getValue(Util.RL(key));
            return Collections.singleton(go);
        }

    }
}
