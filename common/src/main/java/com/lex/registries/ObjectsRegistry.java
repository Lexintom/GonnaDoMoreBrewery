package com.lex.registries;

import com.lex.MoreBrewery;
import com.lex.blocks.BeerBlock;
import com.lex.items.AlcoholBottle;
import com.lex.items.BeerItem;
import com.lex.items.LiverCheckerItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ObjectsRegistry {


    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MoreBrewery.MOD_ID, Registries.ITEM);
    public static final Registrar<Item> ITEM_REGISTRAR = ITEMS.getRegistrar();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MoreBrewery.MOD_ID, Registries.BLOCK);
    public static final Registrar<Block> BLOCK_REGISTRAR = BLOCKS.getRegistrar();

    public static final RegistrySupplier<Block> CACHACA_BLOCO = BLOCKS.register("cachaca", BeerBlock::new);
    public static final RegistrySupplier<Block> ALCOHOL_BLOCK = BLOCKS.register("alcohol_bottle", BeerBlock::new);
    public static final RegistrySupplier<Block> VODKA_BLOCK = BLOCKS.register("vodka", BeerBlock::new);
    public static final RegistrySupplier<Block> MEAD_BLOCK = BLOCKS.register("mead", BeerBlock::new);

    public static final RegistrySupplier<Item> LIVER_CHECKER =
            ITEMS.register("liver_checker", () -> new LiverCheckerItem(new Item.Properties()));
    public static final RegistrySupplier<Item> CACHACA = ITEMS.register("cachaca", () -> new BeerItem(EffectsRegistry.LIGHTHEAD.get(), CACHACA_BLOCO.get()));
    public static final RegistrySupplier<Item> ALCOHOL = ITEMS.register("alcohol_bottle", () -> new AlcoholBottle(MobEffects.POISON, 60, ALCOHOL_BLOCK.get(), new Item.Properties().stacksTo(64)));
    public static final RegistrySupplier<Item> VODKA = ITEMS.register("vodka", () -> new BeerItem(EffectsRegistry.TOUCH_CONFUSION.get(),VODKA_BLOCK.get()));
    public static final RegistrySupplier<Item> MEAD = ITEMS.register("mead", () -> new BeerItem(EffectsRegistry.SATURATED.get(),MEAD_BLOCK.get()));

    public static void init() {
        BLOCKS.register();
        ITEMS.register();
    }

}
