package snownee.cuisine.impl.rule;

import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.RecipeRule;
import snownee.cuisine.api.registry.Cookware;
import snownee.cuisine.cookware.CookwareModule;
import snownee.cuisine.cookware.tile.OvenTile;

public class OvenRule implements RecipeRule<OvenTile> {

    @Override
    public boolean acceptCookware(Cookware cookware) {
        return cookware == CookwareModule.OVEN_TYPE;
    }

    @Override
    public boolean apply(FoodBuilder<OvenTile> input) {
        input.getContext();
        return false;
    }

}
