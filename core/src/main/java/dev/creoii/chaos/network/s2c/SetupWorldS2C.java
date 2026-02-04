package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record SetupWorldS2C(int width, int height, long seed, float ambientLight) {
    public static final Codec<SetupWorldS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("width").forGetter(SetupWorldS2C::width),
            Codec.INT.fieldOf("height").forGetter(SetupWorldS2C::height),
            Codec.LONG.fieldOf("seed").orElse(-1L).forGetter(SetupWorldS2C::seed),
            Codec.FLOAT.fieldOf("ambient_light").orElse(1f).forGetter(SetupWorldS2C::ambientLight)
        ).apply(instance, SetupWorldS2C::new);
    });

    public static void write(Output output, SetupWorldS2C o) {
        output.writeInt(o.width);
        output.writeInt(o.height);
        output.writeLong(o.seed);
        output.writeFloat(o.ambientLight);
    }

    public static SetupWorldS2C read(Input input) {
        return new SetupWorldS2C(input.readInt(), input.readInt(), input.readLong(), input.readFloat());
    }
}
