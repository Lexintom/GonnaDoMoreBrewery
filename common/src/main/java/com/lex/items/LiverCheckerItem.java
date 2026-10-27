package com.lex.items;

import com.lex.utils.LiverHelper;
import com.lex.utils.MoreDrunk;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LiverCheckerItem extends Item {

    private static final String LAST_INTEGRITY_TAG = "LastIntegrity";
    private static final String LAST_TOLERANCE_TAG = "LastTolerance";
    private static final String LAST_LEFT_TAG = "LastAlcoholLeft";
    private static final int CHECK_TICKS = 60; // 3 segundos

    public LiverCheckerItem(Properties props) {
        super(props.stacksTo(1));
    }

    private static ChatFormatting colorFor(int integrity) {
        return integrity > 66 ? ChatFormatting.GREEN
                : integrity > 33 ? ChatFormatting.YELLOW : ChatFormatting.RED;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return CHECK_TICKS;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof ServerPlayer player) {
            CompoundTag tag = stack.getOrCreateTag();
            tag.putInt(LAST_TOLERANCE_TAG, MoreDrunk.getTolerance(player));
            tag.putInt(LAST_LEFT_TAG, MoreDrunk.getAlcoholLeft(player));
            tag.putInt(LAST_INTEGRITY_TAG, LiverHelper.getIntegrity(player));

            player.displayClientMessage(Component.translatable("item.morebrewery.liver_checker.conclusion")
                    .withStyle(ChatFormatting.GREEN), true);
            player.getCooldowns().addCooldown(this, 60);
        }
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(LAST_INTEGRITY_TAG)) {
            int tolerance = tag.getInt(LAST_TOLERANCE_TAG);
            int left = tag.getInt(LAST_LEFT_TAG);
            int integrity = tag.getInt(LAST_INTEGRITY_TAG);

            tooltip.add(Component.translatable("item.morebrewery.liver_checker.tolerance", tolerance)
                    .withStyle(ChatFormatting.AQUA));
            if (left == 0) {
                tooltip.add(Component.translatable("item.morebrewery.liver_checker.maximum_tolerance")
                        .withStyle(ChatFormatting.AQUA));
            } else {
                tooltip.add(Component.translatable("item.morebrewery.liver_checker.alcohol_left", left)
                        .withStyle(ChatFormatting.AQUA));
            }
            tooltip.add(Component.translatable("item.morebrewery.liver_checker.integrity", integrity)
                    .withStyle(colorFor(integrity)));
        } else {
            tooltip.add(Component.translatable("item.morebrewery.liver_checker.none")
                    .withStyle(ChatFormatting.GRAY));
        }
        tooltip.add(Component.translatable("item.morebrewery.liver_checker.hint")
                .withStyle(ChatFormatting.DARK_GRAY));
    }
}