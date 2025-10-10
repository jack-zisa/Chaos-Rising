package dev.creoii.chaos.network.s2c;

import com.mojang.serialization.Codec;

import java.io.Serializable;

public record EntityRemoveS2C(int id) implements Serializable {
    public static final Codec<EntityRemoveS2C> CODEC = Codec.INT.xmap(EntityRemoveS2C::new, EntityRemoveS2C::id);
}
