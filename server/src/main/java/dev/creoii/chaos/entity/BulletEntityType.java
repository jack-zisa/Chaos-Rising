package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.ServerGame;
import dev.creoii.chaos.entity.controller.bullet.path.BulletPath;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.booleanprovider.BooleanProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public record BulletEntityType(String id, float scale, @Nullable String textureId, NumberProvider lifetime, NumberProvider angleOffset, BulletPath path, BooleanProvider piercing) implements EntityType<ServerBulletEntity> {
    public ServerBulletEntity create(ServerGame game, UUID uuid, Vector2 pos, Map<String, Object> customData) {
        ServerBulletEntity bullet = new ServerBulletEntity(this);
        bullet.game = game;
        bullet.uuid = uuid;
        bullet.pos = pos;
        bullet.centerPos = new Vector2();
        bullet.colliderRect = new Rectangle();
        bullet.colliderRect.setPosition(pos);
        bullet.colliderRect.setSize(scale());
        bullet.collidingWith = new HashSet<>();
        bullet.spawnTime = game.getGametime();
        bullet.direction = (Vector2) customData.get("direction");
        bullet.damage = (int) customData.getOrDefault("damage", 0);
        bullet.getCenterPos();
        bullet.lifetime = lifetime.getInt(Provider.Context.of(bullet, game.getGametime()));
        bullet.postSpawn();
        return bullet;
    }
}
