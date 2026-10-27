
package com.lex.utils.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public final class MoreData {

    private MoreData() {
    }

    public static CompoundTag get(Player player) {
        return ((MorePlayerData) player).morebrewery$getData();
    }

    public static void copy(Player from, Player to) {
        ((MorePlayerData) to).morebrewery$setData(get(from).copy());
    }
}
