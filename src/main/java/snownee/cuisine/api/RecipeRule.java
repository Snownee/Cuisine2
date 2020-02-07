package snownee.cuisine.api;

import snownee.cuisine.api.registry.Cookware;

public interface RecipeRule<C> {

    default boolean acceptCookware(Cookware cookware) {
        return true;
    }

    default boolean isFoodRule() {
        return false;
    }

    boolean test(FoodBuilder<C> input);

}
