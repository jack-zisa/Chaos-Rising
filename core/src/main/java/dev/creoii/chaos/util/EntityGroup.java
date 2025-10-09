package dev.creoii.chaos.util;

import com.mojang.serialization.Codec;

public enum EntityGroup {
    CHARACTER,
    ENEMY,
    BULLET,
    LOOT_DROP;

    public static final Codec<EntityGroup> CODEC = Codec.STRING.xmap(s -> EntityGroup.valueOf(s.toUpperCase()), entityGroup -> entityGroup.name().toLowerCase());
}

