package snownee.cuisine.data;

import java.util.Optional;

import com.google.common.collect.TreeMultimap;

import net.minecraftforge.registries.ForgeRegistry;
import snownee.cuisine.api.CuisineRegistries;
import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.registry.Cookware;
import snownee.cuisine.api.registry.CuisineRecipe;

public class CuisineRecipeManager extends CuisineDataManager<CuisineRecipe> {

    /* off */
    private final TreeMultimap<Cookware, CuisineRecipe> recipes = TreeMultimap.create(
            (a, b) -> Integer.compare(CuisineRegistries.COOKWARES.getID(a), CuisineRegistries.COOKWARES.getID(a)),
            (a, b) -> Integer.compare(a.getPriority(), b.getPriority())
    );
    /* on */

    public CuisineRecipeManager(String folder, ForgeRegistry<CuisineRecipe> registry) {
        super(folder, registry);
        setCallback(this::onComplete);
    }

    private void onComplete() {
        recipes.clear();
        registry.getValues().stream().forEach(recipe -> recipes.put(recipe.getCookware(), recipe));
    }

    public Optional<CuisineRecipe> findRecipe(FoodBuilder<?> builder) {
        if (builder.getMaterials().isEmpty()) {
            return Optional.empty();
        }
        //TODO hash match
        for (CuisineRecipe recipe : recipes.get(builder.getCookware())) {
            if (recipe.matches(builder)) {
                return Optional.of(recipe);
            }
        }
        return Optional.empty();
    }

}
