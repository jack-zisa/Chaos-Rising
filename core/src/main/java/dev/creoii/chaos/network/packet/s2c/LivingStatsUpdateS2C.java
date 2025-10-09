package dev.creoii.chaos.network.packet.s2c;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.util.stat.StatContainer;

import java.io.Serializable;

public record LivingStatsUpdateS2C(StatContainer statContainer) implements Serializable {
    public static final Codec<LivingStatsUpdateS2C> CODEC = StatContainer.STAT_CODEC.xmap(LivingStatsUpdateS2C::new, LivingStatsUpdateS2C::statContainer);
}
