
package com.lex.utils;

import com.lex.MoreBrewery;
import com.lex.utils.data.MoreData;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.satisfy.brewery.core.item.DrinkBlockItem;

public final class LiverHelper {

    public static final String INTEGRITY_TAG =
            "morebrewery_liver_integrity";

    public static final TagKey<Item> ALCOHOLIC =
            TagKey.create(
                    Registries.ITEM,
                    new ResourceLocation(
                            MoreBrewery.MOD_ID,
                            "alcoholic_drinks"
                    )
            );

    public static final TagKey<Item> LIVER_RESTORING =
            TagKey.create(
                    Registries.ITEM,
                    new ResourceLocation(
                            MoreBrewery.MOD_ID,
                            "liver_restoring"
                    )
            );

    private LiverHelper() {
    }

    public static int getIntegrity(Player player) {
        CompoundTag data = MoreData.get(player);

        return data.contains(INTEGRITY_TAG)
                ? data.getInt(INTEGRITY_TAG)
                : 100;
    }

    public static void setIntegrity(Player player, int value) {
        MoreData.get(player).putInt(
                INTEGRITY_TAG,
                Math.max(0, Math.min(100, value))
        );
    }

    public static void addIntegrity(Player player, int delta) {
        setIntegrity(player, getIntegrity(player) + delta);
    }

    public static boolean isAlcoholic(ItemStack stack) {
        return stack.getItem() instanceof DrinkBlockItem
                || stack.is(ALCOHOLIC);
    }

    public static boolean isWaterBottle(ItemStack stack) {
        return stack.getItem() instanceof PotionItem
                && PotionUtils.getPotion(stack) == Potions.WATER;
    }

    public static boolean isRestoring(ItemStack stack) {
        return isWaterBottle(stack)
                || stack.is(LIVER_RESTORING);
    }
}
