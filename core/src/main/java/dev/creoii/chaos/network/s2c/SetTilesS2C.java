package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record SetTilesS2C(String layer, int x1, int y1, int x2, int y2, String tile) {
    public static final Codec<SetTilesS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("layer").forGetter(SetTilesS2C::layer),
            Codec.INT.fieldOf("x1").forGetter(SetTilesS2C::x1),
            Codec.INT.fieldOf("y1").forGetter(SetTilesS2C::y1),
            Codec.INT.fieldOf("x2").forGetter(SetTilesS2C::x2),
            Codec.INT.fieldOf("y2").forGetter(SetTilesS2C::y2),
            Codec.STRING.fieldOf("tile").forGetter(SetTilesS2C::tile)
        ).apply(instance, SetTilesS2C::new);
    });

    public static void write(Output output, SetTilesS2C o) {
        output.writeString(o.layer);
        output.writeInt(o.x1);
        output.writeInt(o.y1);
        output.writeInt(o.x2);
        output.writeInt(o.y2);
        output.writeString(o.tile);
    }

    public static SetTilesS2C read(Input input) {
        return new SetTilesS2C(input.readString(), input.readInt(), input.readInt(), input.readInt(), input.readInt(), input.readString());
    }
}
