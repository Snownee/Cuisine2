package snownee.cuisine.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.gson.JsonObject;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import snownee.cuisine.data.DeferredReloadListener;
import snownee.cuisine.util.Tweaker;

@Mixin(RecipeManager.class)
public class MixinRecipeManager {
    @Inject(at = @At("HEAD"), method = "deserializeRecipe", cancellable = true)
    private static void disableRecipes(ResourceLocation recipeId, JsonObject json, CallbackInfoReturnable<IRecipe<?>> info) {
        if (Tweaker.isRecipeDisabled(recipeId)) {
            info.setReturnValue(null);
        }
    }

    @Inject(at = @At("HEAD"), method = "apply")
    private void deferApply(Map<ResourceLocation, JsonObject> splashList, IResourceManager resourceManagerIn, IProfiler profilerIn, CallbackInfo info) {
        DeferredReloadListener.INSTANCE.waitForRegistries();
    }
}
