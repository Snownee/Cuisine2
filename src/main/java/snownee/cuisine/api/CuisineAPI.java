package snownee.cuisine.api;

import java.util.Optional;
import java.util.function.Function;

import com.google.gson.JsonDeserializer;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import snownee.cuisine.api.registry.CuisineFood;
import snownee.cuisine.api.registry.Material;
import snownee.cuisine.api.registry.Spice;

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

    public static Optional<CuisineFood> findFood(ItemStack stack) {
        return INSTANCE != null ? INSTANCE.findFood(stack) : Optional.empty();
    }

    public static Optional<CuisineFood> findFood(BlockState state) {
        return INSTANCE != null ? INSTANCE.findFood(state) : Optional.empty();
    }

    public static Optional<Material> findMaterial(ItemStack stack) {
        return INSTANCE != null ? INSTANCE.findMaterial(stack) : Optional.empty();
    }

    public static Optional<Spice> findSpice(ItemStack stack) {
        return INSTANCE != null ? INSTANCE.findSpice(stack) : Optional.empty();
    }

    public static Optional<Spice> findSpice(FluidStack stack) {
        return INSTANCE != null ? INSTANCE.findSpice(stack) : Optional.empty();
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

        Optional<CuisineFood> findFood(ItemStack stack);

        Optional<CuisineFood> findFood(BlockState state);

        Optional<Material> findMaterial(ItemStack stack);

        Optional<Spice> findSpice(ItemStack stack);

        Optional<Spice> findSpice(FluidStack stack);
    }
}
