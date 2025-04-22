package dev.creoii.chaos.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.entity.controller.bullet.path.BulletPath;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class SingleBulletEntity extends BulletEntity {
    private String id;
    private int lifetime;
    private final int angleOffset;
    private final boolean piercing;
    @Nullable
    private GroupBulletEntity bulletGroup;
    private Group sourceGroup;
    private int damage;
    private int index;

    public SingleBulletEntity(String textureId, int lifetime, int angleOffset, BulletPath path, boolean piercing, float scale) {
        super(textureId, path, scale, Group.BULLET);
        this.lifetime = lifetime;
        this.angleOffset = angleOffset;
        this.piercing = piercing;
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

    public int getLifetime() {
        return lifetime;
    }

    @Override
    public void onLoad(Main main) {
        if (main.getGame().getCollisionManager().getCellSize() < getScale())
            main.getGame().getCollisionManager().setCellSize(getScale());
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setSourceGroup(Group sourceGroup) {
        this.sourceGroup = sourceGroup;
    }

    @Nullable
    public GroupBulletEntity getBulletGroup() {
        return bulletGroup;
    }

    public void setGroup(@Nullable GroupBulletEntity bulletGroup) {
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
        if (other instanceof LivingEntity && other.getGroup() != sourceGroup) {
            ((LivingEntity) other).damage(damage);
            if (!piercing) {
                remove();
            }
        }
    }

    @Override
    public void collisionExit(Entity other) {

    }

    @Override
    public void postSpawn() {

    }

    @Override
    public Entity create(Game game, UUID uuid, Vector2 pos, Map<String, Object> customData) {
        SingleBulletEntity entity = new SingleBulletEntity(getTextureId(), lifetime, angleOffset, path.copy(), piercing, getScale() / COORDINATE_SCALE);
        entity.setId(id);
        entity.sprite = new Sprite(game.getTextureManager().getTexture("bullet", entity.getTextureId()));
        entity.sprite.setSize(entity.getScale(), entity.getScale());
        entity.setMoving(true);
        return entity;
    }

    @Override
    public Entity spawn(Game game, UUID uuid, Vector2 pos, Map<String, Object> customData) {
        Entity entity = super.spawn(game, uuid, pos, customData);
        if (entity instanceof SingleBulletEntity bullet) {
            bullet.direction = (Vector2) customData.get("direction");
            bullet.perpendicular = new Vector2(-bullet.direction.y, bullet.direction.x).nor();
            bullet.sprite.setOriginCenter();
            bullet.sprite.setRotation(bullet.direction.angleDeg() - bullet.angleOffset);
            bullet.damage = (int) customData.getOrDefault("damage", 0);
        }
        return entity;
    }

    @Override
    public void tick(int gametime, float delta) {
        super.tick(gametime, delta);

        lifetime--;

        if (lifetime <= 0) {
            remove();
        }
    }
}
