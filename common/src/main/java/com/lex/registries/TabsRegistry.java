package com.lex.registries;

import com.lex.MoreBrewery;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class TabsRegistry {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(MoreBrewery.MOD_ID, Registries.CREATIVE_MODE_TAB);
    public static final RegistrySupplier<CreativeModeTab> BREWERY_TAB = TABS.register("morebrewery", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
            .icon(() -> new ItemStack(ObjectsRegistry.CACHACA.get()))
            .title(Component.translatable("itemGroup.morebrewery.creative_tab"))
            .displayItems(((itemDisplayParameters, output) -> {
                output.accept(ObjectsRegistry.CACHACA.get());
                output.accept(ObjectsRegistry.ALCOHOL.get());
                output.accept(ObjectsRegistry.VODKA.get());
                output.accept(ObjectsRegistry.LIVER_CHECKER.get());
            }))
            .build());

    public static void init() {
        TABS.register();
    }
}
