package dev.creoii.chaos.network.packet.s2c;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.Codecs;
import dev.creoii.chaos.util.EntityGroup;

import java.io.Serializable;
import java.util.UUID;

public record EntitySpawnS2C(UUID uuid, EntityGroup group, float x, float y) implements Serializable {
    public static final Codec<EntitySpawnS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codecs.UUID.fieldOf("uuid").forGetter(EntitySpawnS2C::uuid),
            EntityGroup.CODEC.fieldOf("group").forGetter(EntitySpawnS2C::group),
            Codec.FLOAT.fieldOf("x").forGetter(EntitySpawnS2C::x),
            Codec.FLOAT.fieldOf("y").forGetter(EntitySpawnS2C::y)
        ).apply(instance, EntitySpawnS2C::new);
    });
}
