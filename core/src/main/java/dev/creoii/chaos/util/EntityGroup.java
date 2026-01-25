package dev.creoii.chaos.util;

import com.mojang.serialization.Codec;

public enum EntityGroup {
    CHARACTER(64),
    ENEMY(256),
    OBJECT(512),
    BULLET(1024),
    LOOT_DROP(64);

    public static final Codec<EntityGroup> CODEC = Codec.STRING.xmap(s -> EntityGroup.valueOf(s.toUpperCase()), entityGroup -> entityGroup.name().toLowerCase());
    private final int poolSize;

    EntityGroup(int poolSize) {
        this.poolSize = poolSize;
    }

    public int getPoolSize() {
        return poolSize;
    }
}

