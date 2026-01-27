package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record PlaceSetpieceS2C(String setpiece, int x, int y) {
    public static final Codec<PlaceSetpieceS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("setpiece").forGetter(PlaceSetpieceS2C::setpiece),
            Codec.INT.fieldOf("x").forGetter(PlaceSetpieceS2C::x),
            Codec.INT.fieldOf("y").forGetter(PlaceSetpieceS2C::y)
        ).apply(instance, PlaceSetpieceS2C::new);
    });

    public static void write(Output output, PlaceSetpieceS2C o) {
        output.writeString(o.setpiece);
        output.writeInt(o.x);
        output.writeInt(o.y);
    }

    public static PlaceSetpieceS2C read(Input input) {
        return new PlaceSetpieceS2C(input.readString(), input.readInt(), input.readInt());
    }
}
