package com.lex.utils;

import com.lex.utils.data.MoreData;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public final class LiverHandler {

    // Ajuste à vontade (ou ligue a uma config depois)
    public static final int FLAT_LOSS_PER_ALCOHOLIC_ITEM = 1;
    public static final int LOSS_PER_ALCOHOL_LEVEL = 3;
    public static final int WATER_RESTORE = 5;
    public static final int TAG_RESTORE = 15;
    public static final double MAX_DAMAGE_PENALTY = 0.5;
    public static final double MAX_SPEED_PENALTY = 0.3;
    public static final double MAX_HEALTH_PENALTY = 0.5;

    private static final UUID DAMAGE_ID = UUID.fromString("6f1c1a52-0b6e-4a43-9a51-1d0c7a3e1001");
    private static final UUID SPEED_ID  = UUID.fromString("6f1c1a52-0b6e-4a43-9a51-1d0c7a3e1002");
    private static final UUID HEALTH_ID = UUID.fromString("6f1c1a52-0b6e-4a43-9a51-1d0c7a3e1003");

    private LiverHandler() {}

    public static void init() {
        TickEvent.PLAYER_POST.register(player -> {
            if (player instanceof ServerPlayer sp && sp.tickCount % 20 == 0) applyPenalties(sp);
        });
        PlayerEvent.PLAYER_CLONE.register((oldP, newP, wonGame) -> MoreData.copy(oldP, newP));
    }

    /** Chamado pelo MoreDrunk após beber álcool. */
    public static void onAlcohol(ServerPlayer player, int gainedLevels) {
        int loss = FLAT_LOSS_PER_ALCOHOLIC_ITEM + Math.max(0, gainedLevels) * LOSS_PER_ALCOHOL_LEVEL;
        LiverHelper.addIntegrity(player, -loss);
    }

    /** Chamado pelo LivingEntityMixin ao terminar de usar um item. */
    public static void onItemFinished(ServerPlayer player, ItemStack stack) {
        if (LiverHelper.isAlcoholic(stack)) return; // álcool é tratado no MoreDrunk
        if (LiverHelper.isWaterBottle(stack)) LiverHelper.addIntegrity(player, WATER_RESTORE);
        else if (LiverHelper.isRestoring(stack)) LiverHelper.addIntegrity(player, TAG_RESTORE);
    }

    private static void applyPenalties(ServerPlayer player) {
        double missing = (100 - LiverHelper.getIntegrity(player)) / 100.0;
        apply(player.getAttribute(Attributes.ATTACK_DAMAGE), DAMAGE_ID, "Liver damage", -MAX_DAMAGE_PENALTY * missing);
        apply(player.getAttribute(Attributes.MOVEMENT_SPEED), SPEED_ID, "Liver speed", -MAX_SPEED_PENALTY * missing);
        apply(player.getAttribute(Attributes.MAX_HEALTH), HEALTH_ID, "Liver health", -MAX_HEALTH_PENALTY * missing);
    }

    private static void apply(AttributeInstance inst, UUID id, String name, double amount) {
        if (inst == null) return;
        AttributeModifier old = inst.getModifier(id);
        if (amount > -0.001) {
            if (old != null) inst.removeModifier(id);
            return;
        }
        if (old != null) {
            if (Math.abs(old.getAmount() - amount) < 0.001) return;
            inst.removeModifier(id);
        }
        inst.addTransientModifier(new AttributeModifier(id, name, amount, AttributeModifier.Operation.MULTIPLY_TOTAL));
    }
}