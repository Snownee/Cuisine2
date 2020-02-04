package snownee.cuisine.api;

import java.util.List;
import java.util.Optional;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import snownee.cuisine.api.registry.Cookware;
import snownee.cuisine.api.registry.CuisineRecipe;
import snownee.cuisine.api.registry.Material;
import snownee.cuisine.api.registry.Spice;

public interface FoodBuilder {

    void add(Material material);

    void add(Spice spice, int incr);

    void add(Spice spice);

    boolean has(Object o);

    int count(Object o);

    List<Material> getMaterials();

    Object2IntMap<Spice> getSpices();

    Cookware getCookware();

    default Optional<CuisineRecipe> findRecipe() {
        return CuisineAPI.findRecipe(this);
    }

}
