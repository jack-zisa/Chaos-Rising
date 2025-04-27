package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.util.Identifiable;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public interface EntityType<T extends Entity> extends Identifiable {
    float scale();

    @Nullable String textureId();

    T create(Game game, UUID uuid, Vector2 pos, Map<String, Object> customData);
}
