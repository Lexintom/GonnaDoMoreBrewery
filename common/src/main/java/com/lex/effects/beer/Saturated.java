package com.lex.effects.beer;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;

public class Saturated extends MobEffect {

    private static final int INTERVAL = 100;

    public Saturated() {
        super(MobEffectCategory.BENEFICIAL, 0xFFC0CB);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % INTERVAL == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!(entity instanceof ServerPlayer player)) return;

        int amount = 2 + 2 * amplifier; // fome e saturação
        FoodData food = player.getFoodData();

        food.setFoodLevel(Math.min(20, food.getFoodLevel() + amount));
        // a saturação nunca passa do nível de fome atual
        food.setSaturation(Math.min(food.getFoodLevel(), food.getSaturationLevel() + amount));
    }
}