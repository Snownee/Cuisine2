package snownee.cuisine.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import snownee.cuisine.handler.FoodHandler;

@Mixin(FoodStats.class)
public abstract class MixinFoodStats {

    @Inject(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/FoodStats;addStats(IF)V"), method = "consume", cancellable = true
    )
    public void consumeHook(Item maybeFood, ItemStack stack, CallbackInfo info) {
        FoodHandler.addExtraFoodStats(maybeFood.getFood(), stack, (FoodStats) (Object) this);
        info.cancel();
    }

    //    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/FoodStats;addStats(IF)V"), method = "consume")
    //    public void addStatsProxy(int foodLevelIn, float foodSaturationModifier) {}
}
