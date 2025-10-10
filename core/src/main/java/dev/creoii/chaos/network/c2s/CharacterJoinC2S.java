package dev.creoii.chaos.network.c2s;

import com.mojang.serialization.Codec;

import java.io.Serializable;

public record CharacterJoinC2S() implements Serializable {
    public static final CharacterJoinC2S INSTANCE = new CharacterJoinC2S();
    public static final Codec<CharacterJoinC2S> CODEC = Codec.unit(INSTANCE);
}
