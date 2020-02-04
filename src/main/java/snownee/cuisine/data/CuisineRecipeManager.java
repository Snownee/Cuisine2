package snownee.cuisine.data;

import java.util.Optional;
import java.util.TreeSet;

import net.minecraftforge.registries.ForgeRegistry;
import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.registry.CuisineRecipe;

public class CuisineRecipeManager extends CuisineDataManager<CuisineRecipe> {

    private final TreeSet<CuisineRecipe> recipes = new TreeSet<>((a, b) -> Integer.compare(a.getPriority(), b.getPriority()));

    public CuisineRecipeManager(String folder, ForgeRegistry<CuisineRecipe> registry) {
        super(folder, registry);
        setCallback(this::onComplete);
    }

    private void onComplete() {
        recipes.clear();
        recipes.addAll(registry.getValues());
    }

    public Optional<CuisineRecipe> findRecipe(FoodBuilder builder) {
        //TODO hash match
        for (CuisineRecipe recipe : recipes) {
            if (recipe.matches(builder)) {
                return Optional.of(recipe);
            }
        }
        return Optional.empty();
    }

}
