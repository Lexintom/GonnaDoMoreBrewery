
package com.lex.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.brewery.core.block.BeverageBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public abstract class BeverageBlockMixin {

    @Inject(
            method = "getCollisionShape",
            at = @At("HEAD"),
            cancellable = true
    )
    private void morebrewery$removeBeverageCollision(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context,
            CallbackInfoReturnable<VoxelShape> cir
    ) {
        if (state.getBlock() instanceof BeverageBlock) {
            cir.setReturnValue(Shapes.empty());
        }
    }
}
