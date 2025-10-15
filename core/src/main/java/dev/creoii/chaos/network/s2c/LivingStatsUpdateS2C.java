package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import dev.creoii.chaos.network.PacketUtils;
import dev.creoii.chaos.util.stat.StatContainer;

public record LivingStatsUpdateS2C(StatContainer statContainer) {
    public static final Codec<LivingStatsUpdateS2C> CODEC = StatContainer.STAT_CODEC.xmap(LivingStatsUpdateS2C::new, LivingStatsUpdateS2C::statContainer);

    public static void write(Output output, LivingStatsUpdateS2C o) {
        PacketUtils.writeStatContainer(output, o.statContainer);
    }

    public static LivingStatsUpdateS2C read(Input input) {
        return new LivingStatsUpdateS2C(PacketUtils.readStatContainer(input));
    }
}
