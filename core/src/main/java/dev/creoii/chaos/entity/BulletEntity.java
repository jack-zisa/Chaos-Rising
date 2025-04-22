package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.entity.controller.bullet.BulletController;
import dev.creoii.chaos.entity.controller.bullet.path.BulletPath;
import dev.creoii.chaos.texture.TextureManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class BulletEntity extends Entity implements DataManager.Identifiable {
    private String id;
    protected final BulletPath path;
    private final EntityController<BulletEntity> controller;
    protected Vector2 direction;
    protected Vector2 perpendicular;
    @Nullable
    private GroupBulletEntity bulletGroup;

    public BulletEntity(String textureId, BulletPath path, float scale, Group group) {
        super(textureId, scale, new Vector2(1, 1), group);
        this.path = path;
        controller = new BulletController(this);
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

    @Override
    public Vector2 getPos() {
        if (bulletGroup != null) {
            System.out.println(pos + " | " + bulletGroup.getPos());
            return pos.add(bulletGroup.getPos());
        }
        return pos;
    }

    public BulletPath getPath() {
        return path;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public Vector2 getPerpendicular() {
        return perpendicular;
    }

    @Nullable
    public GroupBulletEntity getBulletGroup() {
        return bulletGroup;
    }

    public void setBulletGroup(@Nullable GroupBulletEntity bulletGroup) {
        this.bulletGroup = bulletGroup;
    }

    @Override
    public Rectangle getColliderRect() {
        if (pos == null || getCollider() == null)
            return null;
        return new Rectangle(pos.x, pos.y, getCollider().x * getScale() * .8f, getCollider().y * getScale() * .8f);
    }

    @Override
    public void collisionEnter(Entity other) {
    }

    @Override
    public void collisionExit(Entity other) {

    }

    @Override
    public void postSpawn() {

    }

    @Override
    public EntityController<?> getController() {
        return controller;
    }

    public static class Serializer<T extends BulletEntity> implements Json.Serializer<T> {
        @Override
        public void write(Json json, T bullet, Class knownType) {
            json.writeObjectStart();
            json.writeValue("id", bullet.id());
            json.writeValue("texture", bullet.getTextureId());
            json.writeValue("scale", bullet.getScale());
            json.writeObjectEnd();
        }

        @Override
        @SuppressWarnings("unchecked")
        public T read(Json json, JsonValue jsonValue, Class aClass) {
            if (jsonValue.has("texture")) {
                String spritePath = jsonValue.getString("texture", TextureManager.DEFAULT_TEXTURE_ID);
                int lifetime = jsonValue.getInt("lifetime", 0);
                int angleOffset = jsonValue.getInt("angle_offset", 45);
                boolean piercing = jsonValue.getBoolean("piercing", false);
                float scale = jsonValue.getFloat("scale", 1f);
                BulletPath bulletPath = BulletPath.parse(jsonValue);
                return (T) new SingleBulletEntity(spritePath, lifetime, angleOffset, bulletPath, piercing, scale);
            } else if (jsonValue.has("bullets")) {
                List<GroupBulletEntity.Entry> bullets = new ArrayList<>();
                JsonValue bulletsValue = jsonValue.get("bullets");
                bulletsValue.forEach(bulletValue -> {
                    BulletEntity bulletEntity = json.readValue(BulletEntity.class, bulletValue.get("bullet"));
                    Vector2 offset = Vector2.Zero.cpy();

                    if (bulletValue.has("offset")) {
                        JsonValue offsetValue = bulletValue.get("offset");
                        if (offsetValue.isArray()) {
                            offset = new Vector2(offsetValue.getFloat(0), offsetValue.getFloat(1));
                        } else if (offsetValue.isObject()) {
                            offset = new Vector2(offsetValue.getFloat("x"), offsetValue.getFloat("y"));
                        }
                    }

                    bullets.add(new GroupBulletEntity.Entry(bulletEntity, offset));
                });
                BulletPath bulletPath = BulletPath.parse(jsonValue);
                return (T) new GroupBulletEntity(bullets, bulletPath);
            }
            return null;
        }
    }
}
