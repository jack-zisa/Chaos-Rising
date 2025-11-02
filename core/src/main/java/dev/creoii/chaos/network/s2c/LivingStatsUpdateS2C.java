package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.network.PacketUtils;
import dev.creoii.chaos.util.stat.StatContainer;

public record LivingStatsUpdateS2C(int id, StatContainer stats) {
    public static final Codec<LivingStatsUpdateS2C> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("id").forGetter(LivingStatsUpdateS2C::id),
        StatContainer.STAT_CODEC.fieldOf("stats").forGetter(LivingStatsUpdateS2C::stats)
    ).apply(instance, LivingStatsUpdateS2C::new));

    public static void write(Output output, LivingStatsUpdateS2C o) {
        output.writeInt(o.id);
        PacketUtils.writeStatContainer(output, o.stats);
    }

    public static LivingStatsUpdateS2C read(Input input) {
        return new LivingStatsUpdateS2C(input.readInt(), PacketUtils.readStatContainer(input));
    }
}
