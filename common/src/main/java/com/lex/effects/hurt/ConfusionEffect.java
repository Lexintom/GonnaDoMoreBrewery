package com.lex.effects.hurt;

import com.lex.handlers.ConfusionTargetGoal;
import com.lex.mixin.MobAccessor;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.goal.GoalSelector;

public class ConfusionEffect extends MobEffect {

    private static final int GOAL_PRIORITY = 0;

    public ConfusionEffect() {
        super(MobEffectCategory.HARMFUL, 0xFF0088);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true; // Apply every tick for immediate effect
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Mob mob && !mob.level().isClientSide) ensureGoal(mob);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap map, int amplifier) {
        super.addAttributeModifiers(entity, map, amplifier);
        if (entity instanceof Mob mob && !mob.level().isClientSide) ensureGoal(mob);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap map, int amplifier) {
        super.removeAttributeModifiers(entity, map, amplifier);
        if (entity instanceof Mob mob && !mob.level().isClientSide) removeGoal(mob);
    }
    private void ensureGoal(Mob mob) {
        GoalSelector selector = ((MobAccessor) mob).lex$getTargetSelector();
        boolean present = selector.getAvailableGoals().stream()
                .anyMatch(w -> w.getGoal() instanceof ConfusionTargetGoal);
        if (!present) {
            selector.addGoal(GOAL_PRIORITY, new ConfusionTargetGoal(mob, this));
        }
    }

    private static void removeGoal(Mob mob) {
        GoalSelector selector = ((MobAccessor) mob).lex$getTargetSelector();
        selector.getAvailableGoals().stream()
                .map(w -> w.getGoal())
                .filter(g -> g instanceof ConfusionTargetGoal)
                .toList()
                .forEach(selector::removeGoal);
    }}