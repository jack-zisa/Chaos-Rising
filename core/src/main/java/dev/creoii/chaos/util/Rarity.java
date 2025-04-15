package dev.creoii.chaos.util;

import com.badlogic.gdx.graphics.Color;

public enum Rarity {
    COMMON(Color.LIGHT_GRAY),
    UNCOMMON(Color.SKY),
    RARE(Color.PINK),
    LEGENDARY(Color.GOLD),
    DIVINE(Color.WHITE);

    private final Color color;

    Rarity(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
