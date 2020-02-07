package snownee.cuisine.impl.rule;

import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.RecipeRule;
import snownee.cuisine.api.registry.Cookware;

public class NotRecipeRule implements RecipeRule {

    private RecipeRule rule;

    public NotRecipeRule(RecipeRule rule) {
        this.rule = rule;
    }

    @Override
    public boolean test(FoodBuilder input) {
        return !rule.test(input);
    }

    @Override
    public boolean isFoodRule() {
        return rule.isFoodRule();
    }

    @Override
    public boolean acceptCookware(Cookware cookware) {
        return rule.acceptCookware(cookware);
    }

}
