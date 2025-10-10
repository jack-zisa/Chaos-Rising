package dev.creoii.chaos.network.s2c;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.stat.Stat;

import java.io.Serializable;

public record LivingStatUpdateS2C(Stat.Type statType, int value) implements Serializable {
    public static final Codec<LivingStatUpdateS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Stat.Type.CODEC.fieldOf("stat_type").forGetter(LivingStatUpdateS2C::statType),
            Codec.INT.fieldOf("value").forGetter(LivingStatUpdateS2C::value)
        ).apply(instance, LivingStatUpdateS2C::new);
    });
}
