package dev.creoii.chaos.network.c2s;

import com.mojang.serialization.Codec;

import java.io.Serializable;

public record CharacterLeaveC2S(int id) implements Serializable {
    public static final Codec<CharacterLeaveC2S> CODEC = Codec.INT.xmap(CharacterLeaveC2S::new, CharacterLeaveC2S::id);
}
