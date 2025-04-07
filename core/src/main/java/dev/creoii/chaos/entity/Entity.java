package dev.creoii.chaos.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    public static final float DEFAULT_SCALE = 32f;
    // template (non-active) fields
    private final float scale;
    private final Vector2 collider;
    private final Group group;
    private boolean active;
    private boolean moving;

    // active fields
    private Game game;
    private Vector2 pos;
    private UUID uuid;

    public Entity(float scale, Vector2 collider, Group group) {
        this.scale = scale;
        this.collider = collider;
        this.group = group;
        active = false;
        moving = false;
    }

    public abstract Entity create(Game game, UUID uuid, Vector2 pos);

    public abstract EntityController<?> getController();

    public void tick(int gametime, float delta) {
        if (getController() != null) {
            getController().control(delta);
        }
    }

    public float getScale() {
        return scale * DEFAULT_SCALE;
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

    Texture texture = new Texture("textures/enemy/skeleton.png");
    Sprite sprite = new Sprite(texture);
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        if (batch != null) {
            sprite.setSize(getScale(), getScale());
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

    public enum Group {
        CHARACTER,
        ENEMY,
        OBSTACLE,
        BULLET,
        ITEM
    }
}
