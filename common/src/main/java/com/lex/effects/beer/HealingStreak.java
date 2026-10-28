package com.lex.effects.beer;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class HealingStreak extends MobEffect {

    private static final int INTERVAL = 200; // 10 seconds = 200 ticks

    public HealingStreak() {
        super(MobEffectCategory.BENEFICIAL, 0x00FF7F); // Spring green color
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % INTERVAL == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.isDeadOrDying()) return;

        // 2 * (1 + amplifier) corações = 4 * (1 + amplifier) pontos de vida
        int healAmount = 2 * (1 + amplifier);
        entity.heal(healAmount);
    }
}