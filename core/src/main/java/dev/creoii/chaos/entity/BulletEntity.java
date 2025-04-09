package dev.creoii.chaos.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.entity.controller.BulletController;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.texture.TextureManager;

import java.util.Map;
import java.util.UUID;

public class BulletEntity extends Entity implements DataManager.Identifiable {
    private final String id;
    private int lifetime;
    private float speed;
    private final float acceleration;
    private final float frequency;
    private final float amplitude;
    private final float arcSpeed;
    private final boolean piercing;
    private final EntityController<BulletEntity> controller;
    private UUID parentId;
    private Vector2 direction;
    private Vector2 perpendicular;
    private int damage;
    private int index;

    public BulletEntity(String id, String textureId, int lifetime, float speed, float acceleration, float frequency, float amplitude, float arcSpeed, boolean piercing, float scale) {
        super(textureId, scale, new Vector2(1, 1), Group.BULLET);
        this.id = id;
        this.lifetime = lifetime;
        this.speed = speed;
        this.acceleration = acceleration;
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.arcSpeed = arcSpeed;
        this.piercing = piercing;
        controller = new BulletController(this);
        damage = 0;
        index = -1;
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

    public float getSpeed() {
        return speed;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public float getFrequency() {
        return frequency;
    }

    public float getAmplitude() {
        return amplitude;
    }

    public float getArcSpeed() {
        return arcSpeed;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public Vector2 getPerpendicular() {
        return perpendicular;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    @Override
    public Rectangle getColliderRect() {
        if (!isActive()) return null;
        return new Rectangle(pos.x, pos.y, getCollider().x * getScale() * .8f, getCollider().y * getScale() * .8f);
    }

    @Override
    public void collide(Entity other) {
        if (other instanceof LivingEntity && other.getUuid() != parentId) {
            ((LivingEntity) other).damage(damage);
            if (!piercing) {
                remove();
            }
        }
    }

    @Override
    public Entity create(Game game, UUID uuid, Vector2 pos) {
        BulletEntity entity = new BulletEntity(id, getTextureId(), lifetime, speed, acceleration, frequency, amplitude, arcSpeed, piercing, getScale() / COORDINATE_SCALE);
        entity.sprite = new Sprite(game.getTextureManager().getTexture("bullet", entity.getTextureId()));
        entity.sprite.setSize(entity.getScale(), entity.getScale());
        entity.setMoving(true);
        return entity;
    }

    @Override
    public Entity spawn(Game game, UUID uuid, Vector2 pos, Map<String, Object> customData) {
        Entity entity = super.spawn(game, uuid, pos, customData);
        if (entity instanceof BulletEntity bullet) {
            bullet.direction = (Vector2) customData.get("direction");
            bullet.perpendicular = new Vector2(-bullet.direction.y, bullet.direction.x).nor();
            bullet.sprite.setRotation(bullet.direction.angleDeg() - 45);
            bullet.damage = (int) customData.getOrDefault("damage", 0);
        }
        return entity;
    }

    @Override
    public EntityController<?> getController() {
        return controller;
    }

    @Override
    public void tick(int gametime, float delta) {
        super.tick(gametime, delta);
        lifetime--;
        speed += acceleration;

        if (lifetime <= 0) {
            remove();
        }
    }

    public static class Serializer implements Json.Serializer<BulletEntity> {
        @Override
        public void write(Json json, BulletEntity bullet, Class knownType) {
            json.writeObjectStart();
            json.writeValue("id", bullet.id());
            json.writeValue("texture", bullet.getTextureId());
            json.writeValue("lifetime", bullet.lifetime);
            json.writeValue("speed", bullet.speed);
            json.writeValue("acceleration", bullet.acceleration);
            json.writeValue("frequency", bullet.frequency);
            json.writeValue("amplitude", bullet.amplitude);
            json.writeValue("arc_speed", bullet.arcSpeed);
            json.writeValue("piercing", bullet.piercing);
            json.writeValue("scale", bullet.getScale());
            json.writeObjectEnd();
        }

        @Override
        public BulletEntity read(Json json, JsonValue jsonValue, Class aClass) {
            String id = jsonValue.getString("id");
            String spritePath = jsonValue.getString("texture", TextureManager.DEFAULT_TEXTURE_ID);
            int lifetime = jsonValue.getInt("lifetime", 0);
            float speed = jsonValue.getFloat("speed", 0f);
            float acceleration = jsonValue.getFloat("acceleration", 0f);
            float frequency = jsonValue.getFloat("frequency", 0f);
            float amplitude = jsonValue.getFloat("amplitude", 0f);
            float arcSpeed = jsonValue.getFloat("arc_speed", 0f);
            boolean piercing = jsonValue.getBoolean("piercing", false);
            float scale = jsonValue.getFloat("scale", 1f);
            return new BulletEntity(id, spritePath, lifetime, speed, acceleration, frequency, amplitude, arcSpeed, piercing, scale);
        }
    }
}
