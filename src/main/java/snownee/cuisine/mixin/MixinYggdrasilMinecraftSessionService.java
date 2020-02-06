package snownee.cuisine.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;

import snownee.cuisine.Cuisine;

@Mixin(YggdrasilMinecraftSessionService.class)
public class MixinYggdrasilMinecraftSessionService {

    @Inject(at = @At("HEAD"), method = "fillGameProfile", remap = false)
    protected void injectFillGameProfile(final GameProfile profile, final boolean requireSecure, CallbackInfoReturnable<GameProfile> info) {
        Cuisine.logger.info("Debug mode, bypass network operations");
        info.setReturnValue(profile);
    }

}
