package dev.creoii.chaos.network.packet.s2c;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.Codecs;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

public record EntityMoveS2C(UUID uuid, float x, float y, float xv, float yv) implements Serializable {
    public static final Codec<EntityMoveS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codecs.UUID.fieldOf("uuid").forGetter(EntityMoveS2C::uuid),
            Codec.FLOAT.optionalFieldOf("x").forGetter(entityMoveS2C -> entityMoveS2C.x == 0f ? Optional.empty() : Optional.of(entityMoveS2C.x)),
            Codec.FLOAT.optionalFieldOf("y").forGetter(entityMoveS2C -> entityMoveS2C.y == 0f ? Optional.empty() : Optional.of(entityMoveS2C.y)),
            Codec.FLOAT.optionalFieldOf("xv").forGetter(entityMoveS2C -> entityMoveS2C.xv == 0f ? Optional.empty() : Optional.of(entityMoveS2C.xv)),
            Codec.FLOAT.optionalFieldOf("yv").forGetter(entityMoveS2C -> entityMoveS2C.yv == 0f ? Optional.empty() : Optional.of(entityMoveS2C.yv))
        ).apply(instance, (uuid, x, y, xv, yv) -> new EntityMoveS2C(uuid, x.orElse(0f), y.orElse(0f), xv.orElse(0f), yv.orElse(0f)));
    });
}
