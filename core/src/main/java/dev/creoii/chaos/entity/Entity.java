package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;

import java.util.UUID;

public class Entity {
    public static final float COORDINATE_SCALE = 32f;
    private final Game game;
    private final UUID uuid;
    private final Vector2 pos;
    private float scale;

    public Entity(Game game, UUID uuid, Vector2 pos, float scale) {
        this.game = game;
        this.uuid = uuid;
        this.pos = pos;
        this.scale = scale;
    }

    public Game getGame() {
        return game;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(float x, float y) {
        pos.set(x, y);
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
