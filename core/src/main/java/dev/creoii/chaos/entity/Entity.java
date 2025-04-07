package dev.creoii.chaos.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.ai.controller.EntityController;
import dev.creoii.chaos.render.Renderer;
import dev.creoii.chaos.util.Positioned;
import dev.creoii.chaos.util.Tickable;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class Entity implements Positioned, Tickable {
    // template (non-active) fields
    private final Vector2 collider;
    private final Group group;
    private boolean active;
    private boolean moving;

    // active fields
    private Game game;
    private Vector2 pos;
    private UUID uuid;

    public Entity(Vector2 collider, Group group) {
        this.collider = collider;
        this.group = group;
        active = false;
        moving = false;
    }

    public abstract Entity create(Game game, UUID uuid, Vector2 pos);

    public abstract EntityController<?> getController();

    public void tick(int gametime) {
        if (getController() != null) {
            getController().control(1f);
        }
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

    public Game getGame() {
        return game;
    }

    @Override
    public Vector2 getPos() {
        return pos;
    }

    @Override
    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Rectangle getColliderRect() {
        if (!active) return null;
        return new Rectangle(pos.x, pos.y, collider.x, collider.y);
    }

    public Entity spawn(Game game, UUID uuid, Vector2 pos) {
        Entity entity = create(game, uuid, pos);
        entity.game = game;
        entity.uuid = uuid;
        entity.pos = pos;
        entity.setActive();
        return entity;
    }

    public void updateCollision() {
        getColliderRect().setPosition(pos.x, pos.y);
    }

    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        if (debug && shapeRenderer != null) {
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(getColliderRect().x, getColliderRect().y, getColliderRect().width, getColliderRect().height);
        }
    }

    public void remove() {
        game.getEntityManager().removeEntity(this);
    }

    public enum Group {
        CHARACTER,
        ENEMY,
        OBSTACLE,
        BULLET,
        ITEM
    }
}
