package snownee.cuisine.handler;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import snownee.kiwi.util.NBTHelper;

public final class FoodHandler {

    private FoodHandler() {}

    public static void applyExtraFoodEffects(ItemStack stack, World world, LivingEntity living) {
        PotionUtils.getFullEffectsFromItem(stack).forEach(living::addPotionEffect);
    }

    public static void addExtraFoodStats(Food food, ItemStack stack, FoodStats foodStats) {
        NBTHelper data = NBTHelper.of(stack);
        int healing = data.getInt("ExtraHealing");
        float saturation = data.getFloat("ExtraSaturation");
        foodStats.addStats(food.getHealing() + healing, food.getSaturation() + saturation);
    }

}
