package snownee.cuisine.api.registry;

import java.util.List;

import net.minecraftforge.registries.ForgeRegistryEntry;
import snownee.cuisine.api.RecipeRule;

public class CuisineRecipe extends ForgeRegistryEntry<CuisineRecipe> implements Comparable<CuisineRecipe> {

    private CuisineFood result;
    private int priority;
    private Utensil utensil;
    private List<RecipeRule> rules;

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(CuisineRecipe o) {
        return Integer.compare(this.getPriority(), o.getPriority());
    }

    @Override
    public String toString() {
        return "CuisineRecipe{" + getRegistryName() + "}";
    }
}
