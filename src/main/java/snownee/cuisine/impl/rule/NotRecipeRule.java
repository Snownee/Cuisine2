package snownee.cuisine.impl.rule;

import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.RecipeRule;

public class NotRecipeRule implements RecipeRule {

    private RecipeRule rule;

    public NotRecipeRule(RecipeRule rule) {
        this.rule = rule;
    }

    @Override
    public boolean test(FoodBuilder input) {
        return !rule.test(input);
    }

}
