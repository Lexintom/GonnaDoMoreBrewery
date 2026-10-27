package com.lex.mixin;

import com.lex.utils.data.MorePlayerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerAlcoholMixin implements MorePlayerData {

    @Unique
    private CompoundTag morebrewery$data = new CompoundTag();

    @Override
    public CompoundTag morebrewery$getData() { return morebrewery$data; }

    @Override
    public void morebrewery$setData(CompoundTag data) { this.morebrewery$data = data; }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void morebrewery$save(CompoundTag tag, CallbackInfo ci) {
        tag.put("MoreBreweryData", morebrewery$data.copy());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void morebrewery$load(CompoundTag tag, CallbackInfo ci) {
        morebrewery$data = tag.contains("MoreBreweryData") ? tag.getCompound("MoreBreweryData") : new CompoundTag();
    }
}