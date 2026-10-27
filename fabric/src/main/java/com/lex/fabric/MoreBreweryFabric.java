package com.lex.fabric;

import com.lex.commands.HealLiverCommand;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.fabricmc.api.ModInitializer;

import com.lex.MoreBrewery;

public final class MoreBreweryFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Register commands
        CommandRegistrationEvent.EVENT.register(HealLiverCommand::register);

        // Run our common setup.
        MoreBrewery.init();
    }
}
