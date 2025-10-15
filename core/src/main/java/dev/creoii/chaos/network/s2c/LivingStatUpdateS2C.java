package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.network.PacketUtils;
import dev.creoii.chaos.util.stat.Stat;

public record LivingStatUpdateS2C(Stat.Type statType, int value) {
    public static final Codec<LivingStatUpdateS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Stat.Type.CODEC.fieldOf("stat_type").forGetter(LivingStatUpdateS2C::statType),
            Codec.INT.fieldOf("value").forGetter(LivingStatUpdateS2C::value)
        ).apply(instance, LivingStatUpdateS2C::new);
    });

    public static void write(Output output, LivingStatUpdateS2C o) {
        PacketUtils.writeEnum(output, o.statType);
        output.writeInt(o.value);
    }

    public static LivingStatUpdateS2C read(Input input) {
        return new LivingStatUpdateS2C(PacketUtils.readEnum(Stat.Type.class, input), input.readInt());
    }
}
