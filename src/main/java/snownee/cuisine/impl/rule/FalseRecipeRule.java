package snownee.cuisine.impl.rule;

import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.RecipeRule;
import snownee.cuisine.api.registry.Cookware;

public enum FalseRecipeRule implements RecipeRule {
    INSTANCE;

    @Override
    public boolean apply(FoodBuilder input) {
        return false;
    }

}
