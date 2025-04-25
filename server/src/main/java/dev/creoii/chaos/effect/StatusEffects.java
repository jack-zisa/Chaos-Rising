package dev.creoii.chaos.effect;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.HashMap;
import java.util.Map;

public final class StatusEffects {
    public static final Map<String, StatusEffect> ALL = new HashMap<>();
    public static final Map<String, Sprite> EFFECT_TEXTURES = new HashMap<>();

    public static void loadTextures(Game game) {
        ALL.forEach((s, statusEffect) -> {
            Sprite sprite = new Sprite(game.getTextureManager().getTexture("effect", statusEffect.id()));
            EFFECT_TEXTURES.put(s, sprite);
        });
    }

    static {
        StatusEffect.register("regeneration", (entity, statusEffect) -> {
            entity.heal(statusEffect.getAmplifier());
        });
        StatusEffect.register("poison", (entity, statusEffect) -> {
            entity.damage(statusEffect.getAmplifier());
        });
        StatusEffect.register("invulnerable");
    }
}
