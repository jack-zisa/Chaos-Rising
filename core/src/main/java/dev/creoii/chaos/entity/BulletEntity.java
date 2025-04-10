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
import dev.creoii.chaos.util.provider.FloatProvider;
import dev.creoii.chaos.util.provider.Provider;

import java.util.Map;
import java.util.UUID;

public class BulletEntity extends Entity implements DataManager.Identifiable {
    private String id;
    private int lifetime;
    private final int angleOffset;
    private final Provider<Float> speed;
    private final Provider<Float> frequency;
    private final Provider<Float> amplitude;
    private final Provider<Float> arcSpeed;
    private final boolean piercing;
    private final EntityController<BulletEntity> controller;
    private Group parentGroup;
    private Vector2 direction;
    private Vector2 perpendicular;
    private int damage;
    private int index;

    public BulletEntity(String textureId, int lifetime, int angleOffset, Provider<Float> speed, Provider<Float> frequency, Provider<Float> amplitude, Provider<Float> arcSpeed, boolean piercing, float scale) {
        super(textureId, scale, new Vector2(1, 1), Group.BULLET);
        this.lifetime = lifetime;
        this.angleOffset = angleOffset;
        this.speed = speed;
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
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void onLoad(Main main) {
        if (main.getGame().getCollisionManager().getCellSize() < getScale())
            main.getGame().getCollisionManager().setCellSize(getScale());
    }

    public float getSpeed() {
        return speed.get(game);
    }

    public float getFrequency() {
        return frequency.get(game);
    }

    public float getAmplitude() {
        return amplitude.get(game);
    }

    public float getArcSpeed() {
        return arcSpeed.get(game);
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

    public void setParentGroup(Group parentGroup) {
        this.parentGroup = parentGroup;
    }

    @Override
    public Rectangle getColliderRect() {
        if (pos == null || getCollider() == null) return null;
        return new Rectangle(pos.x, pos.y, getCollider().x * getScale() * .8f, getCollider().y * getScale() * .8f);
    }

    @Override
    public void collide(Entity other) {
        if (other instanceof LivingEntity && other.getGroup() != parentGroup) {
            ((LivingEntity) other).damage(damage);
            if (!piercing) {
                remove();
            }
        }
    }

    @Override
    public void postSpawn() {

    }

    @Override
    public Entity create(Game game, UUID uuid, Vector2 pos) {
        BulletEntity entity = new BulletEntity(getTextureId(), lifetime, angleOffset, speed.copy(), frequency.copy(), amplitude.copy(), arcSpeed.copy(), piercing, getScale() / COORDINATE_SCALE);
        entity.setId(id);
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
            bullet.sprite.setOriginCenter();
            bullet.sprite.setRotation(bullet.direction.angleDeg() - bullet.angleOffset);
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
            json.writeValue("angle_offset", bullet.lifetime);
            json.writeValue("speed", bullet.speed);
            json.writeValue("frequency", bullet.frequency);
            json.writeValue("amplitude", bullet.amplitude);
            json.writeValue("arc_speed", bullet.arcSpeed);
            json.writeValue("piercing", bullet.piercing);
            json.writeValue("scale", bullet.getScale());
            json.writeObjectEnd();
        }

        @Override
        public BulletEntity read(Json json, JsonValue jsonValue, Class aClass) {
            String spritePath = jsonValue.getString("texture", TextureManager.DEFAULT_TEXTURE_ID);
            int lifetime = jsonValue.getInt("lifetime", 0);
            int angleOffset = jsonValue.getInt("angle_offset", 45);
            Provider<Float> speed = FloatProvider.parse(jsonValue.get("speed"));
            Provider<Float> frequency = FloatProvider.parse(jsonValue.get("frequency"));
            Provider<Float> amplitude = FloatProvider.parse(jsonValue.get("amplitude"));
            Provider<Float> arcSpeed = FloatProvider.parse(jsonValue.get("arc_speed"));
            boolean piercing = jsonValue.getBoolean("piercing", false);
            float scale = jsonValue.getFloat("scale", 1f);
            return new BulletEntity(spritePath, lifetime, angleOffset, speed, frequency, amplitude, arcSpeed, piercing, scale);
        }
    }
}
