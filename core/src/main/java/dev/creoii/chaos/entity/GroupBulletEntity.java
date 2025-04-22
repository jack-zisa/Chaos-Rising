package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.controller.bullet.path.BulletPath;

import java.util.*;

public class GroupBulletEntity extends BulletEntity {
    private final List<Entry> bullets;

    public GroupBulletEntity(List<Entry> bullets, BulletPath path) {
        super("", path, 1f, Group.GROUP);
        this.bullets = bullets;
    }

    @Override
    public Vector2 getCenterPos() {
        centerPos.set(getPos()).add(COORDINATE_SCALE / 4f, COORDINATE_SCALE / 4f);
        return centerPos;
    }

    @Override
    public Rectangle getColliderRect() {
        return null;
    }

    @Override
    public void collisionEnter(Entity other) {

    }

    @Override
    public void collisionExit(Entity other) {

    }

    @Override
    public void postSpawn() {
        bullets.forEach(entry -> {
            Map<String, Object> customData = new HashMap<>();
            customData.put("direction", getDirection());
            Entity entity = game.getEntityManager().addEntity(entry.bullet, entry.offset.scl(COORDINATE_SCALE).cpy(), customData);
            if (entity instanceof BulletEntity bullet) {
                bullet.setBulletGroup(this);
            }
        });
    }

    @Override
    public Entity create(Game game, UUID uuid, Vector2 pos, Map<String, Object> customData) {
        GroupBulletEntity entity = new GroupBulletEntity(new ArrayList<>(bullets), path.copy());
        entity.setId(id());

        Vector2 direction = (Vector2) customData.get("direction");
        entity.direction = direction;
        entity.perpendicular = new Vector2(-direction.y, direction.x).nor();

        for (int i = 0; i < entity.bullets.size(); ++i) {
            Entry entry = entity.bullets.get(i);
            entry.bullet.setId(id() + i);
            entry.bullet.setBulletGroup(entity);
        }

        entity.setMoving(true);
        return entity;
    }

    @Override
    public Entity spawn(Game game, UUID uuid, Vector2 pos, Map<String, Object> customData) {
        direction = (Vector2) customData.get("direction");
        perpendicular = new Vector2(-direction.y, direction.x).nor();
        return super.spawn(game, uuid, pos, customData);
    }

    public record Entry(BulletEntity bullet, Vector2 offset) { }
}
