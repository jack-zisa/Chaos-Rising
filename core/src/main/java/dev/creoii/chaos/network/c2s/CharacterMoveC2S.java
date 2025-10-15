package dev.creoii.chaos.network.c2s;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public record CharacterMoveC2S(int id, float dx, float dy) {
    public static final Codec<CharacterMoveC2S> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(CharacterMoveC2S::id),
            Codec.FLOAT.optionalFieldOf("dx").forGetter(characterMoveC2S -> characterMoveC2S.dx == 0f ? Optional.empty() : Optional.of(characterMoveC2S.dx)),
            Codec.FLOAT.optionalFieldOf("dy").forGetter(characterMoveC2S -> characterMoveC2S.dy == 0f ? Optional.empty() : Optional.of(characterMoveC2S.dy))
        ).apply(instance, (uuid, dx, dy) -> new CharacterMoveC2S(uuid, dx.orElse(0f), dy.orElse(0f)));
    });

    public static void write(Output output, CharacterMoveC2S o) {
        output.writeInt(o.id);
        output.writeFloat(o.dx);
        output.writeFloat(o.dy);
    }

    public static CharacterMoveC2S read(Input input) {
        return new CharacterMoveC2S(input.readInt(), input.readFloat(), input.readFloat());
    }
}
