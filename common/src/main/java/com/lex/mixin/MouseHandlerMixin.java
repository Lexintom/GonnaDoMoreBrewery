
package com.lex.mixin;

import com.lex.registries.EffectsRegistry;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "net.minecraft.client.MouseHandler")
public abstract class MouseHandlerMixin {

    @ModifyVariable(
            method = "onPress",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0
    )
    private int lex$invertMouseButton(int button) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null ||
                !minecraft.player.hasEffect(EffectsRegistry.CONFUSION.get())) {
            return button;
        }

        if (button == 0) {
            return 1;
        }

        if (button == 1) {
            return 0;
        }

        return button;
    }
}
