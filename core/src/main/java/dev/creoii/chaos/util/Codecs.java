package dev.creoii.chaos.util;

import com.mojang.serialization.Codec;

import java.util.UUID;

public final class Codecs {
    public static final Codec<UUID> UUID = Codec.STRING.xmap(java.util.UUID::fromString, java.util.UUID::toString);
}
