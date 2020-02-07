package snownee.cuisine.util;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.util.ResourceLocation;

public final class Tweaker {
    private Tweaker() {}

    static Set<ResourceLocation> disabledRecipes = Sets.newHashSet();

    public synchronized static void disableRecipe(ResourceLocation id) {
        if (id != null) {
            disabledRecipes.add(id);
        }
    }

    public static boolean isRecipeDisabled(ResourceLocation id) {
        return disabledRecipes.contains(id);
    }
}
