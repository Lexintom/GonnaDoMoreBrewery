package com.lex.handlers;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.player.Player;

import java.util.Comparator;
import java.util.EnumSet;

public class ConfusionTargetGoal extends TargetGoal {

    private static final int INTERVAL = 1;
    private static final double RANGE = 12.0;
    private static final float BASE_CHANCE = 1.0f;
    private static final float CHANCE_PER_LEVEL = 0.0f;
    private static final int PARTICLE_INTERVAL = 5;

    private final MobEffect effect;
    private long nextRoll;
    private int lastHurtStamp;
    private LivingEntity candidate;

    public ConfusionTargetGoal(Mob mob, MobEffect effect) {
        super(mob, false);
        this.effect = effect;
        this.nextRoll = mob.level().getGameTime() + INTERVAL;
        this.lastHurtStamp = mob.getLastHurtByMobTimestamp();
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canUse() {
        MobEffectInstance inst = mob.getEffect(effect);
        if (inst == null) return false;

        long now = mob.level().getGameTime();
        if (now < nextRoll) return false;
        nextRoll = now + INTERVAL;

        float chance = BASE_CHANCE + CHANCE_PER_LEVEL * inst.getAmplifier();
        if (mob.getRandom().nextFloat() >= chance) return false;

        candidate = mob.level()
                .getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(RANGE),
                        e -> e != mob && e.isAlive() && e.attackable()
                                && !(e instanceof Player) && mob.canAttack(e))
                .stream()
                .min(Comparator.comparingDouble(mob::distanceToSqr))
                .orElse(null);

        return candidate != null;
    }

    @Override
    public void start() {
        lastHurtStamp = mob.getLastHurtByMobTimestamp();
        mob.setTarget(candidate);
        makeVictimFightBack();
        spawnAngryParticles(6);
        super.start();
    }

    @Override
    public boolean canContinueToUse() {
        if (!mob.hasEffect(effect)) return false;

        LivingEntity target = mob.getTarget();
        if (target == null || !target.isAlive()) {
            // Target lost or dead, find new one immediately
            return findNewTarget();
        }

        // Check if we were hurt by someone other than current target
        int stamp = mob.getLastHurtByMobTimestamp();
        if (stamp != lastHurtStamp) {
            LivingEntity attacker = mob.getLastHurtByMob();
            if (attacker != null && attacker != candidate && attacker.isAlive() && mob.canAttack(attacker)) {
                // Switch target to the attacker
                candidate = attacker;
                mob.setTarget(attacker);
                makeVictimFightBack();
                spawnAngryParticles(6);
            }
            lastHurtStamp = stamp;
        }

        // Also re-evaluate target periodically (every few ticks) to find closer/better targets
        if (mob.tickCount % 20 == 0) {
            LivingEntity newTarget = findBetterTarget();
            if (newTarget != null && newTarget != candidate) {
                candidate = newTarget;
                mob.setTarget(newTarget);
                makeVictimFightBack();
                spawnAngryParticles(4);
            }
        }

        return candidate != null && candidate.isAlive();
    }

    /**
     * Finds a new target when current one is lost.
     */
    private boolean findNewTarget() {
        LivingEntity newTarget = mob.level()
                .getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(RANGE),
                        e -> e != mob && e.isAlive() && e.attackable()
                                && !(e instanceof Player) && mob.canAttack(e))
                .stream()
                .min(Comparator.comparingDouble(mob::distanceToSqr))
                .orElse(null);

        if (newTarget != null) {
            candidate = newTarget;
            mob.setTarget(newTarget);
            makeVictimFightBack();
            spawnAngryParticles(6);
            return true;
        }
        return false;
    }

    /**
     * Finds a better target (closer or more threatening).
     */
    private LivingEntity findBetterTarget() {
        return mob.level()
                .getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(RANGE),
                        e -> e != mob && e.isAlive() && e.attackable()
                                && !(e instanceof Player) && mob.canAttack(e))
                .stream()
                .min(Comparator.comparingDouble(mob::distanceToSqr))
                .orElse(null);
    }

    @Override
    public void tick() {
        if (candidate == null) return;

        makeVictimFightBack();

        if (mob.tickCount % PARTICLE_INTERVAL == 0) {
            spawnAngryParticles(2);
        }
    }

    @Override
    public void stop() {
        if (candidate instanceof Mob victim && victim.getTarget() == mob) {
            victim.setTarget(null);
        }
        mob.setTarget(null);
        candidate = null;
        nextRoll = mob.level().getGameTime() + INTERVAL;
        super.stop();
    }

    /** Faz o alvo revidar o mob afetado (quando o alvo é um Mob com IA de ataque). */
    private void makeVictimFightBack() {
        if (candidate instanceof Mob victim && victim.getTarget() != mob) {
            victim.setTarget(mob);
        }
    }

    private void spawnAngryParticles(int count) {
        if (!(mob.level() instanceof ServerLevel server) || candidate == null) return;
        spawnAbove(server, mob, count);
        spawnAbove(server, candidate, count);
    }

    private static void spawnAbove(ServerLevel server, LivingEntity e, int count) {
        server.sendParticles(ParticleTypes.ANGRY_VILLAGER,
                e.getX(), e.getY() + e.getBbHeight() + 0.2, e.getZ(),
                count, 0.25, 0.1, 0.25, 0.0);
    }
}