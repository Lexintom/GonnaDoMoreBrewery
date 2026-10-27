package com.lex.mixin;

import com.lex.utils.MoreDrunk;
import net.minecraft.server.level.ServerPlayer;
import net.satisfy.brewery.core.effect.alcohol.AlcoholManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AlcoholManager.class, remap = false)
public abstract class AlcoholManagerMixin {

    @Inject(method = "drinkAlcohol", at = @At("HEAD"), cancellable = true)
    private static void gdm$drinkAlcohol(ServerPlayer player, CallbackInfo ci) {
        if (MoreDrunk.handleDrink(player, player.getUseItem())) {
            ci.cancel();
        }
    }
}
