package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.ServerGame;
import dev.creoii.chaos.entity.behavior.Behavior;
import dev.creoii.chaos.loot.LootTable;
import dev.creoii.chaos.util.stat.StatContainer;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public record EnemyEntityType(String id, float scale, @Nullable String textureId, @Nullable LootTable lootTable, @Nullable Behavior behavior, StatContainer statContainer) implements EntityType<ServerEnemyEntity> {
    public static final StatContainer DEFAULT_STAT_CONTAINER = new StatContainer(10, 1, 1, 0, 1, 1);

    public ServerEnemyEntity create(ServerGame game, UUID uuid, Vector2 pos, Map<String, Object> customData) {
        ServerEnemyEntity enemy = new ServerEnemyEntity(pos.cpy(), this);
        enemy.game = game;
        enemy.uuid = uuid;
        enemy.pos = pos;
        enemy.centerPos = new Vector2();
        enemy.colliderRect = new Rectangle();
        enemy.colliderRect.setPosition(pos);
        enemy.colliderRect.setSize(scale());
        enemy.collidingWith = new HashSet<>();
        enemy.spawnTime = game.getGametime();
        enemy.getCenterPos();
        enemy.postSpawn();
        return enemy;
    }
}
