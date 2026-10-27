package com.lex.effects.beer;

import com.lex.registries.EffectsRegistry;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class TouchOfDrunkness extends MobEffect {

    private static final int CONFUSION_TIME = 100;
    private static final int MAX_CONFUSION_TIME = 600;
    public TouchOfDrunkness() {
        super(MobEffectCategory.BENEFICIAL, 0xFFA500);
    }

    public static void init() {
        EntityEvent.LIVING_HURT.register((victim, source, amount) -> {
            if (victim.level().isClientSide) return EventResult.pass();

            if (source.getEntity() instanceof LivingEntity attacker && source.getDirectEntity() == attacker) {
                MobEffectInstance touch = attacker.getEffect(EffectsRegistry.TOUCH_CONFUSION.get());
                if (touch != null) {
                    int amp = touch.getAmplifier();
                    MobEffectInstance cur = victim.getEffect(EffectsRegistry.CONFUSION.get());

                    int duration = CONFUSION_TIME;
                    if (cur != null) {
                        duration = Math.min(MAX_CONFUSION_TIME, cur.getDuration() + CONFUSION_TIME);
                        amp = Math.max(amp, cur.getAmplifier());
                    }

                    victim.addEffect(new MobEffectInstance(EffectsRegistry.CONFUSION.get(), duration, amp));
                }
            }
            return EventResult.pass();
        });
    }
}