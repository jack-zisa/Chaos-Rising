package dev.creoii.chaos.network.s2c;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.Codecs;

import java.io.Serializable;
import java.util.UUID;

public record EntityDisplayS2C(UUID uuid, String textureId, float scale) implements Serializable {
    public static final Codec<EntityDisplayS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codecs.UUID.fieldOf("uuid").forGetter(EntityDisplayS2C::uuid),
            Codec.STRING.fieldOf("texture_id").forGetter(EntityDisplayS2C::textureId),
            Codec.FLOAT.fieldOf("scale").forGetter(EntityDisplayS2C::scale)
        ).apply(instance, EntityDisplayS2C::new);
    });
}
