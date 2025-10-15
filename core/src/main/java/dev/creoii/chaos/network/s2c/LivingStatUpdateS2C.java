package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import dev.creoii.chaos.network.PacketUtils;
import dev.creoii.chaos.util.stat.Stat;

public record LivingStatUpdateS2C(Stat stat) {
    public static final Codec<LivingStatUpdateS2C> CODEC = Stat.CODEC.xmap(LivingStatUpdateS2C::new, LivingStatUpdateS2C::stat);

    public static void write(Output output, LivingStatUpdateS2C o) {
        PacketUtils.writeStat(output, o.stat);
    }

    public static LivingStatUpdateS2C read(Input input) {
        return new LivingStatUpdateS2C(PacketUtils.readStat(input));
    }
}
