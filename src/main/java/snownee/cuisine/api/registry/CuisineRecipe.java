package snownee.cuisine.api.registry;

import java.util.Collections;
import java.util.List;

import net.minecraftforge.registries.ForgeRegistryEntry;
import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.RecipeRule;

public class CuisineRecipe extends ForgeRegistryEntry<CuisineRecipe> {

    private CuisineFood result;
    private int priority;
    private Cookware utensil;
    private List<RecipeRule> rules = Collections.EMPTY_LIST;

    public int getPriority() {
        return priority;
    }

    public CuisineFood getResult() {
        return result;
    }

    public boolean matches(FoodBuilder builder) {
        return rules.stream().allMatch(rule -> rule.test(builder));
    }

    @Override
    public String toString() {
        return "CuisineRecipe{" + getRegistryName() + "}";
    }
}
