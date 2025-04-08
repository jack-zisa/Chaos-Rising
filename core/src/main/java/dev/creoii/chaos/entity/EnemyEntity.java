package dev.creoii.chaos.entity;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.entity.controller.EnemyController;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.render.Renderer;
import dev.creoii.chaos.texture.TextureManager;
import dev.creoii.chaos.util.stat.Stats;

import javax.annotation.Nullable;
import java.util.UUID;

public class EnemyEntity extends LivingEntity implements DataManager.Identifiable {
    private final String id;
    private final EntityController<EnemyEntity> controller;

    public EnemyEntity(String id, String textureId, float scale) {
        super(textureId, scale, new Vector2(1, 1), Group.ENEMY, new Stats(), new Stats());
        this.id = id;
        controller = new EnemyController(this);
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void onLoad(Main main) {
        if (main.getGame().getCollisionManager().getCellSize() < getScale())
            main.getGame().getCollisionManager().setCellSize(getScale());
    }

    @Override
    public void collide(Entity other) {
        if (other.getGroup() == Group.CHARACTER) {
            ((LivingEntity) other).damage(5);
        }
    }

    @Override
    public void tick(int gametime, float delta) {
        super.tick(gametime, delta);

        if (getStats().health <= 0)
            remove();
    }

    @Override
    public Entity create(Game game, UUID uuid, Vector2 pos) {
        EnemyEntity entity = new EnemyEntity(id, getTextureId(), getScale() / COORDINATE_SCALE);
        entity.sprite = new Sprite(game.getTextureManager().getTexture("enemy", getTextureId()));
        entity.sprite.setSize(getScale(), getScale());
        entity.setMoving(true);
        return entity;
    }

    @Override
    public EntityController<EnemyEntity> getController() {
        return controller;
    }

    @Override
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        super.render(renderer, batch, shapeRenderer, font, debug);

        if (debug && batch != null) {
            font.draw(batch, getStats().health + "/" + getMaxStats().health, pos.x, pos.y);
        }
    }

    public static class Serializer implements Json.Serializer<EnemyEntity> {
        @Override
        public void write(Json json, EnemyEntity enemy, Class knownType) {
            json.writeObjectStart();
            json.writeValue("id", enemy.id());
            json.writeValue("scale", enemy.getScale());
            json.writeValue("texture", enemy.getTextureId());
            json.writeObjectEnd();
        }

        @Override
        public EnemyEntity read(Json json, JsonValue jsonValue, Class aClass) {
            String id = jsonValue.getString("id");
            String spritePath = jsonValue.getString("texture", TextureManager.DEFAULT_TEXTURE_ID);
            float scale = jsonValue.getFloat("scale", 1f);
            return new EnemyEntity(id, spritePath, scale);
        }
    }
}
