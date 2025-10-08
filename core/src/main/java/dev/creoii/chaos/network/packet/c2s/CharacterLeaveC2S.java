package dev.creoii.chaos.network.packet.c2s;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.util.Codecs;

import java.io.Serializable;
import java.util.UUID;

public record CharacterLeaveC2S(UUID uuid) implements Serializable {
    public static final Codec<CharacterLeaveC2S> CODEC = Codecs.UUID.xmap(CharacterLeaveC2S::new, CharacterLeaveC2S::uuid);
}
