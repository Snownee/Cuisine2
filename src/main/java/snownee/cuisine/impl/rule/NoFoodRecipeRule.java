package snownee.cuisine.impl.rule;

import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.RecipeRule;

public enum NoFoodRecipeRule implements RecipeRule {
    INSTANCE;

    @Override
    public boolean test(FoodBuilder input) {
        return input.getFoods().isEmpty();
    }

    @Override
    public boolean isFoodRule() {
        return true;
    }

}
