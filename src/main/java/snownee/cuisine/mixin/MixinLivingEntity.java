package snownee.cuisine.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import snownee.cuisine.handler.FoodHandler;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    @Inject(at = @At("HEAD"), method = "applyFoodEffects")
    public void applyFoodEffectsHook(ItemStack stack, World world, LivingEntity living, CallbackInfo info) {
        FoodHandler.applyExtraFoodEffects(stack, world, living);
    }

}
