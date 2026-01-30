package dev.creoii.chaos.network.c2s;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record CharacterMoveStartC2S(int id, boolean axis, boolean positive) {
    public static final Codec<CharacterMoveStartC2S> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(CharacterMoveStartC2S::id),
            Codec.BOOL.fieldOf("axis").forGetter(CharacterMoveStartC2S::axis),
            Codec.BOOL.fieldOf("positive").forGetter(CharacterMoveStartC2S::positive)
        ).apply(instance, CharacterMoveStartC2S::new);
    });

    public static void write(Output output, CharacterMoveStartC2S o) {
        output.writeInt(o.id);
        output.writeBoolean(o.axis);
        output.writeBoolean(o.positive);
    }

    public static CharacterMoveStartC2S read(Input input) {
        return new CharacterMoveStartC2S(input.readInt(), input.readBoolean(), input.readBoolean());
    }
}
