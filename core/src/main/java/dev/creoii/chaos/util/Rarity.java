package dev.creoii.chaos.util;

import com.badlogic.gdx.graphics.Color;
import com.mojang.serialization.Codec;

public enum Rarity {
    COMMON(Color.LIGHT_GRAY),
    UNCOMMON(Color.SKY),
    RARE(Color.PINK),
    LEGENDARY(Color.GOLD),
    DIVINE(Color.WHITE);

    public static final Codec<Rarity> CODEC = Codec.STRING.xmap(s -> Rarity.valueOf(s.toUpperCase()), entityGroup -> entityGroup.name().toLowerCase());
    private final Color color;

    Rarity(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
