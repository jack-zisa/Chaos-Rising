package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.network.PacketUtils;
import dev.creoii.chaos.util.stat.Stat;

public record LivingStatUpdateS2C(int id, Stat stat, boolean setMax) {
    public static final Codec<LivingStatUpdateS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(LivingStatUpdateS2C::id),
            Stat.CODEC.fieldOf("stat").forGetter(LivingStatUpdateS2C::stat),
            Codec.BOOL.fieldOf("set_max").orElse(true).forGetter(LivingStatUpdateS2C::setMax)
        ).apply(instance, LivingStatUpdateS2C::new);
    });

    public LivingStatUpdateS2C(int id, Stat stat) {
        this(id, stat, true);
    }

    public static void write(Output output, LivingStatUpdateS2C o) {
        output.writeInt(o.id);
        PacketUtils.writeStat(output, o.stat);
        output.writeBoolean(o.setMax);
    }

    public static LivingStatUpdateS2C read(Input input) {
        return new LivingStatUpdateS2C(input.readInt(), PacketUtils.readStat(input), input.readBoolean());
    }
}
