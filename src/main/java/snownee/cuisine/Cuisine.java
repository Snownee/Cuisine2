package snownee.cuisine;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializer;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import snownee.cuisine.api.Bonus;
import snownee.cuisine.api.CuisineAPI.ICuisineAPI;
import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.RecipeRule;
import snownee.cuisine.api.ResearchInfo;
import snownee.cuisine.api.registry.Cookware;
import snownee.cuisine.api.registry.CuisineFood;
import snownee.cuisine.api.registry.CuisineRecipe;
import snownee.cuisine.api.registry.Material;
import snownee.cuisine.api.registry.Spice;
import snownee.cuisine.api.tile.ISpiceHandler;
import snownee.cuisine.base.tile.SpiceHandler;
import snownee.cuisine.data.CuisineDataManager;
import snownee.cuisine.impl.FoodBuilderImpl;
import snownee.kiwi.KiwiModule.LoadingCondition;
import snownee.kiwi.LoadingContext;
import snownee.kiwi.util.NBTHelper;

@Mod("cuisine")
public final class Cuisine implements ICuisineAPI {
    public static final String NAME = "Cuisine";

    public static boolean mixin;
    public static Logger logger = LogManager.getLogger(Cuisine.NAME);
    static MinecraftServer server;

    public Cuisine() {
        set(this);
    }

    public static MinecraftServer getServer() {
        return server;
    }

    @LoadingCondition("core")
    public static boolean checkMixin(LoadingContext ctx) {
        if (!mixin) {
            throw new IllegalStateException("Cannot find Mixin!");
        }
        return mixin;
    }

    @Override
    public int getFoodStar(ItemStack stack) {
        return NBTHelper.of(stack).getInt("FoodStar");
    }

    @Override
    public ItemStack setFoodStar(ItemStack stack, int star) {
        return NBTHelper.of(stack).setInt("FoodStar", star).getItem();
    }

    @Override
    public synchronized void registerBonusAdapter(String key, JsonDeserializer<? extends Bonus> adapter) {
        CuisineDataManager.bonusAdapters.put(key, (JsonDeserializer<Bonus>) adapter);
    }

    @Override
    public synchronized void registerRecipeRuleAdapter(String key, JsonDeserializer<? extends RecipeRule> adapter) {
        CuisineDataManager.ruleAdapters.put(key, (JsonDeserializer<RecipeRule>) adapter);
    }

    private static final List<Function<ItemStack, Optional<CuisineFood>>> specialFoodMatchers = Lists.newLinkedList();

    @Override
    public synchronized void registerSpecialFoodMatcher(Function<ItemStack, Optional<CuisineFood>> matcher) {
        specialFoodMatchers.add(matcher);
    }

    @Override
    public Optional<CuisineFood> findFood(ItemStack stack) {
        if (stack.isEmpty())
            return Optional.empty();
        CuisineFood food = CoreModule.item2Food.get(stack.getItem());
        if (food == null) {
            for (Function<ItemStack, Optional<CuisineFood>> func : specialFoodMatchers) {
                Optional<CuisineFood> result = func.apply(stack);
                if (result.isPresent()) {
                    return result;
                }
            }
        }
        return Optional.ofNullable(food);
    }

    @Override
    public Optional<CuisineFood> findFood(BlockState state) {
        return Optional.ofNullable(CoreModule.block2Food.get(state.getBlock()));
    }

    @Override
    public Optional<Material> findMaterial(ItemStack stack) {
        if (stack.isEmpty())
            return Optional.empty();
        return Optional.ofNullable(CoreModule.item2Material.get(stack.getItem()));
    }

    @Override
    public Optional<Spice> findSpice(ItemStack stack) {
        if (stack.isEmpty())
            return Optional.empty();
        return Optional.ofNullable(CoreModule.item2Spice.get(stack.getItem()));
    }

    @Override
    public Optional<Spice> findSpice(FluidStack stack) {
        if (stack.isEmpty())
            return Optional.empty();
        return Optional.ofNullable(CoreModule.fluid2Spice.get(stack.getFluid()));
    }

    @Override
    public <C> FoodBuilder<C> foodBuilder(Cookware cookware, C context, Entity cook) {
        return new FoodBuilderImpl<>(cookware, context, cook);
    }

    @Override
    public Optional<CuisineRecipe> findRecipe(FoodBuilder foodBuilder) {
        return CoreModule.recipeManager.findRecipe(foodBuilder);
    }

    @Override
    public ResearchInfo getResearchInfo(Entity entity) {
        return CoreModule.getResearchData().get(entity);
    }

    @Override
    public ISpiceHandler newSpiceHandler() {
        return new SpiceHandler();
    }
}
