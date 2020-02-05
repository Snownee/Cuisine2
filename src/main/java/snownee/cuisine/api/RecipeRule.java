package snownee.cuisine.api;

import com.google.common.base.Predicate;

import snownee.cuisine.api.registry.Cookware;

public interface RecipeRule<C> extends Predicate<FoodBuilder<C>> {

    default boolean acceptCookware(Cookware cookware) {
        return true;
    }

    @Override
    boolean apply(FoodBuilder<C> input);

}
