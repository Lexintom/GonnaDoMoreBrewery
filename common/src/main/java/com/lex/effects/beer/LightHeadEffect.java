package com.lex.effects.beer;

import com.lex.registries.EffectsRegistry;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class LightHeadEffect extends MobEffect {

    public LightHeadEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x00FF00);
    }

    public static void init() {
        EntityEvent.LIVING_HURT.register((victim, source, amount) -> {
            if (source.getEntity() != null && rolls(victim)) {
                return EventResult.interruptFalse();
            }
            if (source.getEntity() instanceof LivingEntity attacker && rolls(attacker)) {
                return EventResult.interruptFalse();
            }
            return EventResult.pass();
        });
    }

    private static boolean rolls(LivingEntity entity) {
        MobEffectInstance inst = entity.getEffect(EffectsRegistry.LIGHTHEAD.get());
        if (inst == null) return false;
        int chance = Math.min(10, inst.getAmplifier() + 1);
        return entity.getRandom().nextInt(10) < chance;
    }
}