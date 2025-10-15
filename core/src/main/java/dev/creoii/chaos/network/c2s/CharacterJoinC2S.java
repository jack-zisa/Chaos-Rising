package dev.creoii.chaos.network.c2s;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;

public record CharacterJoinC2S() {
    public static final CharacterJoinC2S INSTANCE = new CharacterJoinC2S();
    public static final Codec<CharacterJoinC2S> CODEC = Codec.unit(INSTANCE);

    public static CharacterJoinC2S read(Input input) {
        return INSTANCE;
    }

    public static void write(Output output, Object o) {
    }
}
