package dev.creoii.chaos.network.c2s;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record CharacterMoveEndC2S(int id, boolean axis, boolean positive) {
    public static final Codec<CharacterMoveEndC2S> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(CharacterMoveEndC2S::id),
            Codec.BOOL.fieldOf("axis").forGetter(CharacterMoveEndC2S::axis),
            Codec.BOOL.fieldOf("positive").forGetter(CharacterMoveEndC2S::positive)
        ).apply(instance, CharacterMoveEndC2S::new);
    });

    public static void write(Output output, CharacterMoveEndC2S o) {
        output.writeInt(o.id);
        output.writeBoolean(o.axis);
        output.writeBoolean(o.positive);
    }

    public static CharacterMoveEndC2S read(Input input) {
        return new CharacterMoveEndC2S(input.readInt(), input.readBoolean(), input.readBoolean());
    }
}
