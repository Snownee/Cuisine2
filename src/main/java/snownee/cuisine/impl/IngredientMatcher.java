package snownee.cuisine.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import snownee.cuisine.api.registry.CuisineFood;

public class IngredientMatcher implements Function<ItemStack, Optional<CuisineFood>> {

    private final Ingredient ingredient;
    private final CuisineFood food;

    public IngredientMatcher(Ingredient ingredient, CuisineFood food) {
        this.ingredient = ingredient;
        this.food = food;
    }

    @Override
    public Optional<CuisineFood> apply(ItemStack input) {
        if (ingredient.test(input)) {
            return Optional.of(food);
        } else {
            return Optional.absent();
        }
    }

}
