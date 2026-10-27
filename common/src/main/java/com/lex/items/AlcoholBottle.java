package com.lex.items;

import com.lex.utils.MaxDrunk;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AlcoholBottle extends BeerItem implements MaxDrunk {

    public AlcoholBottle(MobEffect effect, int duration, Block block, Properties settings) {
        super(effect, block);

    }


    @Override
    public boolean setMaxDrunk() {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        super.appendHoverText(stack, world, tooltip, context);
        tooltip.add(Component.translatable("item.morebrewery.hint.max_alcohol").withStyle(ChatFormatting.DARK_RED));
    }
}
