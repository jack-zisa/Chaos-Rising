package dev.creoii.chaos.network.packet.s2c;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.Codecs;

import java.io.Serializable;
import java.util.UUID;

public record EntityMoveS2C(UUID uuid, float x, float y, float xv, float yv) implements Serializable {
    public static final Codec<EntityMoveS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codecs.UUID.fieldOf("uuid").forGetter(EntityMoveS2C::uuid),
            Codec.FLOAT.fieldOf("x").forGetter(EntityMoveS2C::x),
            Codec.FLOAT.fieldOf("y").forGetter(EntityMoveS2C::y),
            Codec.FLOAT.fieldOf("xv").forGetter(EntityMoveS2C::xv),
            Codec.FLOAT.fieldOf("yv").forGetter(EntityMoveS2C::yv)
        ).apply(instance, EntityMoveS2C::new);
    });
}
