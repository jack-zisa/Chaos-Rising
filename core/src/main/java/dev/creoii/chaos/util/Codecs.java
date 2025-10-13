package dev.creoii.chaos.util;

import com.badlogic.gdx.graphics.Color;
import com.mojang.serialization.Codec;

import java.util.UUID;

public final class Codecs {
    public static final Codec<UUID> UUID = Codec.STRING.xmap(java.util.UUID::fromString, java.util.UUID::toString);

    public static final Codec<Color> COLOR = Codec.STRING.xmap(Color::valueOf, Color::toString);
}
