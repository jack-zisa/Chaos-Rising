package dev.creoii.chaos.network.c2s;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;

public record CharacterStopMoveC2S(int id) {
    public static final Codec<CharacterStopMoveC2S> CODEC = Codec.INT.xmap(CharacterStopMoveC2S::new, CharacterStopMoveC2S::id);

    public static void write(Output output, CharacterStopMoveC2S o) {
        output.writeInt(o.id);
    }

    public static CharacterStopMoveC2S read(Input input) {
        return new CharacterStopMoveC2S(input.readInt());
    }
}
