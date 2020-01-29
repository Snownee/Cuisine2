package snownee.cuisine.api;

import java.util.Optional;
import java.util.function.Function;

import com.google.gson.JsonDeserializer;

import net.minecraft.item.ItemStack;
import snownee.cuisine.api.registry.CuisineFood;

public final class CuisineAPI {

    private CuisineAPI() {}

    public static int getMaterialStar(ItemStack stack) {
        return INSTANCE != null ? INSTANCE.getMaterialStar(stack) : 0;
    }

    public static ItemStack setMaterialStar(ItemStack stack, int star) {
        return INSTANCE != null ? INSTANCE.setMaterialStar(stack, star) : stack;
    }

    public static int getFoodStar(ItemStack stack) {
        return INSTANCE != null ? INSTANCE.getFoodStar(stack) : 0;
    }

    public static ItemStack setFoodStar(ItemStack stack, int star) {
        return INSTANCE != null ? INSTANCE.setFoodStar(stack, star) : stack;
    }

    public static void registerBonusAdapter(String key, JsonDeserializer<? extends Bonus> adapter) {
        if (INSTANCE != null)
            INSTANCE.registerBonusAdapter(key, adapter);
    }

    public static void registerRecipeRuleAdapter(String key, JsonDeserializer<? extends RecipeRule> adapter) {
        if (INSTANCE != null)
            INSTANCE.registerRecipeRuleAdapter(key, adapter);
    }

    public static Optional<CuisineFood> getFood(ItemStack stack) {
        return INSTANCE != null ? INSTANCE.getFood(stack) : Optional.empty();
    }

    private static ICuisineAPI INSTANCE = null;

    public interface ICuisineAPI {
        default void set(ICuisineAPI instance) {
            INSTANCE = instance;
        }

        int getFoodStar(ItemStack stack);

        ItemStack setFoodStar(ItemStack stack, int star);

        int getMaterialStar(ItemStack stack);

        ItemStack setMaterialStar(ItemStack stack, int star);

        void registerBonusAdapter(String key, JsonDeserializer<? extends Bonus> adapter);

        void registerRecipeRuleAdapter(String key, JsonDeserializer<? extends RecipeRule> adapter);

        void registerSpecialFoodMatcher(Function<ItemStack, Optional<CuisineFood>> matcher);

        Optional<CuisineFood> getFood(ItemStack stack);
    }
}
