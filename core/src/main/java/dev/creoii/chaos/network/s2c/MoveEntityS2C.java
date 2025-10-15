package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record MoveEntityS2C(int id, float x, float y, float xv, float yv) {
    public static final Codec<MoveEntityS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(MoveEntityS2C::id),
            Codec.FLOAT.fieldOf("x").forGetter(MoveEntityS2C::x),
            Codec.FLOAT.fieldOf("y").forGetter(MoveEntityS2C::y),
            Codec.FLOAT.fieldOf("xv").forGetter(MoveEntityS2C::xv),
            Codec.FLOAT.fieldOf("yv").forGetter(MoveEntityS2C::yv)
        ).apply(instance, MoveEntityS2C::new);
    });

    public static void write(Output output, MoveEntityS2C o) {
        output.writeInt(o.id);
        output.writeFloat(o.x);
        output.writeFloat(o.y);
        output.writeFloat(o.xv);
        output.writeFloat(o.yv);
    }

    public static MoveEntityS2C read(Input input) {
        return new MoveEntityS2C(input.readInt(), input.readFloat(), input.readFloat(), input.readFloat(), input.readFloat());
    }
}
