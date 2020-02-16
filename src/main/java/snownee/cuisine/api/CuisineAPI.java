package snownee.cuisine.api;

import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.gson.JsonDeserializer;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import snownee.cuisine.api.registry.Cookware;
import snownee.cuisine.api.registry.CuisineFood;
import snownee.cuisine.api.registry.CuisineFoodInstance;
import snownee.cuisine.api.registry.CuisineRecipe;
import snownee.cuisine.api.registry.Material;
import snownee.cuisine.api.registry.Spice;
import snownee.cuisine.api.tile.IMasterHandler;

public final class CuisineAPI {
    public static final String MODID = "cuisine";
    public static final Random RAND = new Random(114514);

    private CuisineAPI() {}

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

    public static void registerSpecialFoodMatcher(Function<ItemStack, Optional<CuisineFood>> matcher) {
        if (INSTANCE != null)
            INSTANCE.registerSpecialFoodMatcher(matcher);
    }

    public static Optional<CuisineFood> findFood(ItemStack stack) {
        return INSTANCE != null ? INSTANCE.findFood(stack) : Optional.empty();
    }

    public static Optional<CuisineFoodInstance> findFoodInstance(ItemStack stack) {
        return INSTANCE != null ? INSTANCE.findFoodInstance(stack) : Optional.empty();
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

    public static <C> FoodBuilder<C> foodBuilder(Cookware cookware, C context, Entity cook) {
        return INSTANCE.foodBuilder(cookware, context, cook);
    }

    public static Optional<CuisineRecipe> findRecipe(FoodBuilder foodBuilder) {
        return INSTANCE != null ? INSTANCE.findRecipe(foodBuilder) : Optional.empty();
    }

    public static ResearchInfo getResearchInfo(Entity entity) {
        return entity != null && INSTANCE != null ? INSTANCE.getResearchInfo(entity) : ResearchInfo.Empty.INSTANCE;
    }

    public static IMasterHandler newSpiceHandler() {
        return INSTANCE != null ? INSTANCE.newSpiceHandler() : null;
    }

    private static ICuisineAPI INSTANCE = null;

    public interface ICuisineAPI {
        default void set(ICuisineAPI instance) {
            INSTANCE = instance;
        }

        int getFoodStar(ItemStack stack);

        ItemStack setFoodStar(ItemStack stack, int star);

        void registerBonusAdapter(String key, JsonDeserializer<? extends Bonus> adapter);

        void registerRecipeRuleAdapter(String key, JsonDeserializer<? extends RecipeRule> adapter);

        void registerSpecialFoodMatcher(Function<ItemStack, Optional<CuisineFood>> matcher);

        Optional<CuisineFood> findFood(ItemStack stack);

        default Optional<CuisineFoodInstance> findFoodInstance(ItemStack stack) {
            return findFood(stack).map(food -> new CuisineFoodInstance(food, getFoodStar(stack)));
        }

        Optional<CuisineFood> findFood(BlockState state);

        Optional<Material> findMaterial(ItemStack stack);

        Optional<Spice> findSpice(ItemStack stack);

        Optional<Spice> findSpice(FluidStack stack);

        <C> FoodBuilder<C> foodBuilder(Cookware cookware, C context, @Nullable Entity cook);

        Optional<CuisineRecipe> findRecipe(FoodBuilder foodBuilder);

        ResearchInfo getResearchInfo(Entity entity);

        IMasterHandler newSpiceHandler();
    }
}
