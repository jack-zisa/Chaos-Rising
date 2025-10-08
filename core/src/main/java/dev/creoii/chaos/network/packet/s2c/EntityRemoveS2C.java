package dev.creoii.chaos.network.packet.s2c;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.util.Codecs;

import java.io.Serializable;
import java.util.UUID;

public record EntityRemoveS2C(UUID uuid) implements Serializable {
    public static final Codec<EntityRemoveS2C> CODEC = Codecs.UUID.xmap(EntityRemoveS2C::new, EntityRemoveS2C::uuid);
}
