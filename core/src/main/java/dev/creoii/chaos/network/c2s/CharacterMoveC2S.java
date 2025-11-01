package dev.creoii.chaos.network.c2s;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record CharacterMoveC2S(int id, boolean axis, boolean positive) {
    public static final Codec<CharacterMoveC2S> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(CharacterMoveC2S::id),
            Codec.BOOL.fieldOf("axis").forGetter(CharacterMoveC2S::axis),
            Codec.BOOL.fieldOf("positive").forGetter(CharacterMoveC2S::positive)
        ).apply(instance, CharacterMoveC2S::new);
    });

    public static void write(Output output, CharacterMoveC2S o) {
        output.writeInt(o.id);
        output.writeBoolean(o.axis);
        output.writeBoolean(o.positive);
    }

    public static CharacterMoveC2S read(Input input) {
        return new CharacterMoveC2S(input.readInt(), input.readBoolean(), input.readBoolean());
    }
}
