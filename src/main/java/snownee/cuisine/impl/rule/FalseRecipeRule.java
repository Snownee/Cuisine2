package snownee.cuisine.impl.rule;

import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.RecipeRule;

public enum FalseRecipeRule implements RecipeRule {
    INSTANCE;

    @Override
    public boolean test(FoodBuilder input) {
        return false;
    }

}
