package snownee.cuisine.api;

import snownee.cuisine.api.registry.Cookware;

public interface RecipeRule<C> {

    default boolean acceptCookware(Cookware cookware) {
        return true;
    }

    boolean test(FoodBuilder<C> input);

}
