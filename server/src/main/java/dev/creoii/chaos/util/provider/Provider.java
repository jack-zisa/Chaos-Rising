package dev.creoii.chaos.util.provider;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.ServerEntity;

import java.util.Random;

public interface Provider<T> {
    T get(Context context);

    record Context(Game game, ServerEntity sourceEntity, int startTime, Vector2 startPos, Random random) {
        public static Context of(ServerEntity sourceEntity, int startTime) {
            return new Context(sourceEntity.getGame(), sourceEntity, startTime, sourceEntity.getPos(), new Random());
        }
    }
}
