package com.lex.registries;

import com.lex.MoreBrewery;
import com.lex.effects.beer.HealingStreak;
import com.lex.effects.beer.LightHeadEffect;
import com.lex.effects.beer.Saturated;
import com.lex.effects.beer.TouchOfDrunkness;
import com.lex.effects.hurt.ConfusionEffect;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;

public class EffectsRegistry {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(MoreBrewery.MOD_ID, Registries.MOB_EFFECT);
    public static final Registrar<MobEffect> MOB_EFFECT_REGISTRAR = MOB_EFFECTS.getRegistrar();

    public static final RegistrySupplier<MobEffect> LIGHTHEAD = MOB_EFFECTS.register("lighthead", LightHeadEffect::new);
    public static final RegistrySupplier<MobEffect> TOUCH_CONFUSION = MOB_EFFECTS.register("drunk_touch", TouchOfDrunkness::new);
    public static final RegistrySupplier<MobEffect> CONFUSION = MOB_EFFECTS.register("confusion", ConfusionEffect::new);
    public static final RegistrySupplier<MobEffect> HEALING_STREAK = MOB_EFFECTS.register("healing_streak", HealingStreak::new);
    public static final RegistrySupplier<MobEffect> SATURATED = MOB_EFFECTS.register("saturated", Saturated::new);


    public static void init() {

        LightHeadEffect.init();
        TouchOfDrunkness.init();
        MOB_EFFECTS.register();
    }

}


