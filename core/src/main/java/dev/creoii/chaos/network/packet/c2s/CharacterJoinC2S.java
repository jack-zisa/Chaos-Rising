package dev.creoii.chaos.network.packet.c2s;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.util.Codecs;

import java.io.Serializable;
import java.util.UUID;

public record CharacterJoinC2S(UUID uuid) implements Serializable {
    public static final Codec<CharacterJoinC2S> CODEC = Codecs.UUID.xmap(CharacterJoinC2S::new, CharacterJoinC2S::uuid);
}
