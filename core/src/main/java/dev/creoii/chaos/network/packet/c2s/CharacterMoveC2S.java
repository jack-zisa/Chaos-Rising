package dev.creoii.chaos.network.packet.c2s;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.Codecs;

import java.io.Serializable;
import java.util.UUID;

public record CharacterMoveC2S(UUID uuid, float dx, float dy) implements Serializable {
    public static final Codec<CharacterMoveC2S> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codecs.UUID.fieldOf("uuid").forGetter(CharacterMoveC2S::uuid),
            Codec.FLOAT.fieldOf("dx").forGetter(CharacterMoveC2S::dx),
            Codec.FLOAT.fieldOf("dy").forGetter(CharacterMoveC2S::dy)
        ).apply(instance, CharacterMoveC2S::new);
    });
}
