package dev.creoii.chaos.util.provider;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.Entity;

import java.util.Random;

public interface Provider<T> {
    T get(Context context);

    default Provider<T> optimize() {
        return this;
    }

    class Context {
        private final Game game;
        private final Entity entity;
        private int time;
        private final Vector2 pos;
        private final Random random;

        public Context(Game game, Entity entity, int time, Vector2 pos, Random random) {
            this.game = game;
            this.entity = entity;
            this.time = time;
            this.pos = pos;
            this.random = random;
        }

        public Game game() {
            return game;
        }

        public Entity entity() {
            return entity;
        }

        public int time() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public Vector2 pos() {
            return pos;
        }

        public Random random() {
            return random;
        }

        public static Context of(Entity sourceEntity, int startTime) {
            return new Context(sourceEntity.getGame(), sourceEntity, startTime, sourceEntity.getPos(), sourceEntity.getGame().getRandom());
        }
    }
}
