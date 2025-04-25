package dev.creoii.chaos.util.provider;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.Entity;

import java.util.Random;

public interface Provider<T> {
    T get(Context context);

    record Context(Game game, Entity sourceEntity, int startTime, Vector2 startPos, Random random) {
        public static Context of(Entity sourceEntity, int startTime) {
            return new Context(sourceEntity.getGame(), sourceEntity, startTime, sourceEntity.getPos(), new Random());
        }
    }
}
