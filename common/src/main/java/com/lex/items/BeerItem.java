package com.lex.items;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.satisfy.brewery.core.item.DrinkBlockItem;

public class BeerItem extends DrinkBlockItem {
    public BeerItem(MobEffect effect, Block block) {
        super(effect, 600, block,new Item.Properties().food(
                new FoodProperties.Builder()
                        .nutrition(2)
                        .saturationMod(1.0F)
                        .build()
        ));
    }
}
