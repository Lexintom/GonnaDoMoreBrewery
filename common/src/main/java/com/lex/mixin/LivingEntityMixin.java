package com.lex.mixin;

import com.lex.utils.LiverHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow protected ItemStack useItem;

    @Inject(method = "completeUsingItem", at = @At("HEAD"))
    private void morebrewery$onFinish(CallbackInfo ci) {
        if ((Object) this instanceof ServerPlayer player && !useItem.isEmpty()) {
            LiverHandler.onItemFinished(player, useItem);
        }
    }
}