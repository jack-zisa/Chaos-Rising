package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record SetTileS2C(String layer, int x, int y, String tile) {
    public static final Codec<SetTileS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("layer").forGetter(SetTileS2C::layer),
            Codec.INT.fieldOf("x").forGetter(SetTileS2C::x),
            Codec.INT.fieldOf("y").forGetter(SetTileS2C::y),
            Codec.STRING.fieldOf("tile").forGetter(SetTileS2C::tile)
        ).apply(instance, SetTileS2C::new);
    });

    public static void write(Output output, SetTileS2C o) {
        output.writeString(o.layer);
        output.writeInt(o.x);
        output.writeInt(o.y);
        output.writeString(o.tile);
    }

    public static SetTileS2C read(Input input) {
        return new SetTileS2C(input.readString(), input.readInt(), input.readInt(), input.readString());
    }
}
