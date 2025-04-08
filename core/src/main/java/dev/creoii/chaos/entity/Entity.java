package dev.creoii.chaos.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.render.Renderer;
import dev.creoii.chaos.util.Positionable;
import dev.creoii.chaos.util.Tickable;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class Entity implements Positionable, Tickable {
    public static final float COORDINATE_SCALE = 32f;
    // template (non-active) fields
    private final float scale;
    private final String textureId;
    private final Vector2 collider;
    private final Group group;
    private boolean active;
    private boolean moving;
    private long spawnTime;

    // active fields
    private Game game;
    private Vector2 pos;
    private UUID uuid;
    protected Sprite sprite;

    public Entity(String textureId, float scale, Vector2 collider, Group group) {
        this.scale = scale;
        this.textureId = textureId;
        this.collider = collider;
        this.group = group;
        active = false;
        moving = false;
        spawnTime = -1;
    }

    public abstract Entity create(Game game, UUID uuid, Vector2 pos);

    public abstract EntityController<?> getController();

    public abstract void collide(Entity other);

    public void tick(int gametime, float delta) {
        if (getController() != null) {
            getController().control(delta);
        }
    }

    public float getScale() {
        return scale * COORDINATE_SCALE;
    }

    public String getTextureId() {
        return textureId;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Rectangle getColliderRect() {
        if (!active) return null;
        return new Rectangle(pos.x, pos.y, collider.x * getScale(), collider.y * getScale());
    }

    public Group getGroup() {
        return group;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive() {
        this.active = true;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public long getSpawnTime() {
        return spawnTime;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public Vector2 getPos() {
        return pos;
    }

    public Vector2 getCenterPos() {
        Vector2 center = new Vector2(getPos()).add(COORDINATE_SCALE / 2f, COORDINATE_SCALE / 2f);
        sprite.setCenter(center.x, center.y);
        return center;
    }

    @Override
    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Entity spawn(Game game, UUID uuid, Vector2 pos) {
        Entity entity = create(game, uuid, pos);
        entity.game = game;
        entity.uuid = uuid;
        entity.pos = pos;
        entity.spawnTime = TimeUtils.millis();
        entity.setActive();
        return entity;
    }

    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        if (batch != null) {
            sprite.setPosition(getPos().x, getPos().y);
            sprite.draw(batch);
        }

        if (debug && shapeRenderer != null) {
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(getColliderRect().x, getColliderRect().y, getColliderRect().width, getColliderRect().height);
        }
    }

    public void remove() {
        game.getEntityManager().removeEntity(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Entity entity) {
            return entity.getUuid().equals(getUuid());
        }
        return false;
    }

    public enum Group {
        CHARACTER,
        ENEMY,
        OBSTACLE,
        BULLET,
        ITEM
    }
}
