
package com.lex.mixin;

import com.lex.registries.EffectsRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Input.class)
public abstract class InputMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void lex$invertMovement(
            boolean slowDown,
            float slowDownFactor,
            CallbackInfo ci
    ) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null ||
                !minecraft.player.hasEffect(EffectsRegistry.CONFUSION.get())) {
            return;
        }

        Input input = (Input) (Object) this;

        boolean oldUp = input.up;
        boolean oldDown = input.down;
        boolean oldLeft = input.left;
        boolean oldRight = input.right;

        input.up = oldDown;
        input.down = oldUp;
        input.left = oldRight;
        input.right = oldLeft;

        input.forwardImpulse = -input.forwardImpulse;
        input.leftImpulse = -input.leftImpulse;
    }
}
