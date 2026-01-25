package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record GainExperienceS2C(int id, int experience, int level) {
    public static final Codec<GainExperienceS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(GainExperienceS2C::id),
            Codec.INT.fieldOf("experience").forGetter(GainExperienceS2C::experience),
            Codec.INT.fieldOf("level").forGetter(GainExperienceS2C::level)
        ).apply(instance, GainExperienceS2C::new);
    });

    public static void write(Output output, GainExperienceS2C o) {
        output.writeInt(o.id);
        output.writeInt(o.experience);
        output.writeInt(o.level);
    }

    public static GainExperienceS2C read(Input input) {
        return new GainExperienceS2C(input.readInt(), input.readInt(), input.readInt());
    }
}
