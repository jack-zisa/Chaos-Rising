package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.ServerGame;

import javax.annotation.Nullable;
import java.util.Map;

public interface EntityType<T extends Entity> extends DataManager.Identifiable {
    float scale();

    @Nullable String textureId();

    T create(ServerGame game, Vector2 pos, Map<String, Object> customData);
}
