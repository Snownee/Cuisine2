package snownee.cuisine.impl;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import snownee.cuisine.api.CuisineRegistries;
import snownee.cuisine.api.registry.CuisineFood;

public class IngredientMatcher implements Function<ItemStack, Optional<CuisineFood>> {

    private final Predicate<ItemStack> ingredient;
    private final ResourceLocation foodId;

    public IngredientMatcher(Predicate<ItemStack> ingredient, ResourceLocation foodId) {
        this.ingredient = ingredient;
        this.foodId = foodId;
    }

    @Override
    public Optional<CuisineFood> apply(ItemStack input) {
        if (ingredient.test(input)) {
            return Optional.ofNullable(CuisineRegistries.FOODS.getValue(foodId));
        } else {
            return Optional.empty();
        }
    }

}
