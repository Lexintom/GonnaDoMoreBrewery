package com.lex.effects.hurt;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

import java.util.Comparator;
import java.util.Map;
import java.util.WeakHashMap;

public class ConfusionEffect extends MobEffect {

    private static final int INTERVAL = 100;
    private static final double RANGE = 12.0;
    private static final float BASE_CHANCE = 0.10f;
    private static final float CHANCE_PER_LEVEL = 0.10f;

    private static final Map<Mob, State> STATES = new WeakHashMap<>();

    public ConfusionEffect() {
        super(MobEffectCategory.HARMFUL, 0xFF0088);
    }

    public static void init() {

    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 5 == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!(entity instanceof Mob mob) || mob.level().isClientSide) return;

        long now = mob.level().getGameTime();
        State s = STATES.computeIfAbsent(mob, m -> {
            State st = new State();
            st.nextRoll = now + INTERVAL;
            st.lastHurtStamp = m.getLastHurtByMobTimestamp();
            return st;
        });

        // Já está fixado em um alvo: mantém até o alvo morrer ou o mob levar dano.
        if (s.target != null) {
            boolean damaged = mob.getLastHurtByMobTimestamp() != s.lastHurtStamp;

            if (!s.target.isAlive() || damaged) {
                release(mob, s, now);
            } else {
                if (mob.getTarget() != s.target) mob.setTarget(s.target);
                return;
            }
        }

        s.lastHurtStamp = mob.getLastHurtByMobTimestamp();

        if (now < s.nextRoll) return;
        s.nextRoll = now + INTERVAL;

        float chance = BASE_CHANCE + CHANCE_PER_LEVEL * amplifier;
        if (mob.getRandom().nextFloat() >= chance) return;

        LivingEntity newTarget = mob.level()
                .getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(RANGE),
                        e -> e != mob && e.isAlive() && !(e instanceof Player))
                .stream()
                .min(Comparator.comparingDouble(mob::distanceToSqr))
                .orElse(null);

        if (newTarget != null) {
            s.target = newTarget;
            s.lastHurtStamp = mob.getLastHurtByMobTimestamp();
            mob.setTarget(newTarget);
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);

        if (entity instanceof Mob mob && !mob.level().isClientSide) {
            State s = STATES.remove(mob);
            if (s != null && s.target != null) {
                mob.setTarget(null);
            }
        }
    }

    private static void release(Mob mob, State s, long now) {
        s.target = null;
        s.nextRoll = now + INTERVAL;
        mob.setTarget(null);
    }

    private static final class State {
        long nextRoll;
        int lastHurtStamp;
        LivingEntity target;
    }
}