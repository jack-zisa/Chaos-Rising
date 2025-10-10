package dev.creoii.chaos.network.s2c;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.io.Serializable;

public record EntityDisplayS2C(int id, String textureId, float scale) implements Serializable {
    public static final Codec<EntityDisplayS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(EntityDisplayS2C::id),
            Codec.STRING.fieldOf("texture_id").forGetter(EntityDisplayS2C::textureId),
            Codec.FLOAT.fieldOf("scale").forGetter(EntityDisplayS2C::scale)
        ).apply(instance, EntityDisplayS2C::new);
    });
}
