package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.behavior.bulletpath.BulletPath;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.provider.booleanprovider.BooleanProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

import java.util.Map;
import java.util.UUID;

public record BulletEntityType(String id, float scale, int lifetime, NumberProvider angleOffset, BulletPath path, BooleanProvider piercing) implements EntityType<BulletEntity> {
    @Override
    public EntityGroup group() {
        return EntityGroup.BULLET;
    }

    public BulletEntity create(Game game, UUID uuid, Vector2 pos, Map<String, Object> customData) {
        BulletEntity bullet = new BulletEntity(game, this, uuid, pos.cpy(), (Vector2) customData.get("direction"), lifetime, (int) customData.get("damage"), (int) customData.get("index"));
        /*bullet.centerPos = new Vector2();
        bullet.colliderRect = new Rectangle();
        bullet.colliderRect.setPosition(pos);
        bullet.colliderRect.setSize(scale());
        bullet.collidingWith = new HashSet<>();
        bullet.spawnTime = game.getGametime();*/
        /*bullet.getCenterPos();*/
        /*bullet.postSpawn();*/
        return bullet;
    }
}
