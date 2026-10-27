package com.lex.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.lex.MoreBrewery;

@Mod(MoreBrewery.MOD_ID)
public final class ExampleModForge {
    public ExampleModForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(MoreBrewery.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        MoreBrewery.init();
    }
}
