package com.lex;

import com.lex.registries.EffectsRegistry;
import com.lex.registries.ObjectsRegistry;
import com.lex.registries.TabsRegistry;
import com.lex.utils.LiverHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MoreBrewery {
    public static final String MOD_ID = "morebrewery";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        EffectsRegistry.init();
        registerEvents();
        ObjectsRegistry.init();
        TabsRegistry.init();
        LiverHandler.init();

    }

    private static void registerEvents() {
       }
}
