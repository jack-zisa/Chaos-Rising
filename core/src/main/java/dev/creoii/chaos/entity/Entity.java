package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.util.Tickable;

import java.util.UUID;

public abstract class Entity implements Tickable {
    public static final float COORDINATE_SCALE = 32f;
    private final Game game;
    private final EntityType<? extends Entity> type;
    private final UUID uuid;
    private final Vector2 pos;
    private final Vector2 prevPos;
    private final int spawnTime;

    public Entity(Game game, EntityType<? extends Entity> type, UUID uuid, Vector2 pos) {
        this.game = game;
        this.type = type;
        this.uuid = uuid;
        this.pos = pos.cpy();
        prevPos = pos.cpy();
        spawnTime = game.getGametime();
    }

    public Game getGame() {
        return game;
    }

    public EntityType<? extends Entity> getType() {
        return type;
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

    public Vector2 getPrevPos() {
        return prevPos;
    }

    public void setPrevPos(float x, float y) {
        prevPos.set(x, y);
    }

    public int getSpawnTime() {
        return spawnTime;
    }

    @Override
    public void tick(int gametime, float delta) {

    }
}
