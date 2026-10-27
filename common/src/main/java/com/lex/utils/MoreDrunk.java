package com.lex.utils;

import com.lex.utils.data.MoreData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.satisfy.brewery.core.effect.alcohol.AlcoholLevel;
import net.satisfy.brewery.core.effect.alcohol.AlcoholManager;
import net.satisfy.brewery.core.effect.alcohol.AlcoholPlayer;
import net.satisfy.brewery.core.registry.MobEffectRegistry;

public final class MoreDrunk {

    private static final String ALCOHOL_TAG = "morebrewery_alcohol_consumed";
    private static final String PROGRESS_TAG = "morebrewery_drink_progress";
    private static final int ALCOHOL_PER_TOLERANCE = 10;
    private static final int MAX_TOLERANCE = 10;

    private MoreDrunk() {}

    public static int getTolerance(Player player) {
        int alcohol = MoreData.get(player).getInt(ALCOHOL_TAG);
        return Math.min(alcohol / ALCOHOL_PER_TOLERANCE, MAX_TOLERANCE);
    }

    public static int getAlcoholLeft(Player player) {
        if (getTolerance(player) >= MAX_TOLERANCE) return 0;

        int alcohol = MoreData.get(player).getInt(ALCOHOL_TAG);
        return ALCOHOL_PER_TOLERANCE - (alcohol % ALCOHOL_PER_TOLERANCE);
    }

    public static boolean handleDrink(ServerPlayer player, ItemStack drink) {
        if (!(player instanceof AlcoholPlayer alcoholPlayer)) return false;

        AlcoholLevel level = alcoholPlayer.brewery$getAlcohol();
        CompoundTag data = MoreData.get(player);
        int drunkBefore = level.getDrunkenness();

        int consumed = Math.min(data.getInt(ALCOHOL_TAG) + 1, MAX_TOLERANCE * ALCOHOL_PER_TOLERANCE);
        data.putInt(ALCOHOL_TAG, consumed);

        if (drink.getItem() instanceof MaxDrunk potency && potency.setMaxDrunk()) {
            int missing = level.getImmunity() + 1 - level.getDrunkenness(); // blackout
            if (missing > 0) level.drink(missing);
        } else {
            int required = 1 + getTolerance(player);
            int progress = data.getInt(PROGRESS_TAG) + 1;
            while (progress >= required) {
                level.drink(1);
                progress -= required;
            }
            data.putInt(PROGRESS_TAG, progress);
        }
        LiverHandler.onAlcohol(player, level.getDrunkenness() - drunkBefore);
        syncDrunkEffect(player, level);
        return true;
    }

    public static void syncDrunkEffect(ServerPlayer player, AlcoholLevel level) {
        int drunkenness = level.getDrunkenness();

        if (drunkenness > 0) {
            player.addEffect(new MobEffectInstance(MobEffectRegistry.DRUNK.get(), AlcoholManager.DRUNK_TIME,
                    drunkenness - 1, false, level.isDrunk()));
        } else {
            player.removeEffect(MobEffectRegistry.DRUNK.get());
        }

        if (level.isBlackout() && !player.hasEffect(MobEffectRegistry.BLACKOUT.get())) {
            player.addEffect(new MobEffectInstance(MobEffectRegistry.BLACKOUT.get(), 15 * 20, 0, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 13 * 20, 0, false, false));
        }

        AlcoholManager.syncAlcohol(player, level);
    }
}
