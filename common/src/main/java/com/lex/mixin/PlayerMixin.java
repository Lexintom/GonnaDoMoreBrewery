package com.lex.mixin;

import com.lex.MoreBrewery;
import com.lex.utils.data.MorePlayerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin implements MorePlayerData {

    @Unique
    private CompoundTag morebrewery$data = new CompoundTag();


    @Override
    public CompoundTag morebrewery$getData() {
        return morebrewery$data;
    }


    @Override
    public void morebrewery$setData(CompoundTag data) {
        this.morebrewery$data = data;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void morebrewery$save(CompoundTag tag, CallbackInfo ci) {
        tag.put(MoreBrewery.MOD_ID, morebrewery$data.copy());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void morebrewery$load(CompoundTag tag, CallbackInfo ci) {
        morebrewery$data = tag.getCompound(MoreBrewery.MOD_ID).copy();

        // Migração de saves antigos em Forge: dados estavam em getPersistentData() ("ForgeData")
        if (tag.contains("ForgeData", Tag.TAG_COMPOUND)) {
            CompoundTag legacy = tag.getCompound("ForgeData");
            for (String key : legacy.getAllKeys()) {
                if (key.startsWith("morebrewery_") && !morebrewery$data.contains(key)) {
                    morebrewery$data .put(key, legacy.get(key).copy());
                }
            }
        }
    }
}
