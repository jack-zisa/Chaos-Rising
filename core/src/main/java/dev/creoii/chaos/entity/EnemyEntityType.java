package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.behavior.Behavior;
import dev.creoii.chaos.loot.LootTable;
import dev.creoii.chaos.util.stat.StatContainer;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public record EnemyEntityType(String id, float scale, @Nullable String textureId, @Nullable LootTable lootTable, @Nullable Behavior behavior, StatContainer statContainer) implements EntityType<EnemyEntity> {
    public static final StatContainer DEFAULT_STAT_CONTAINER = new StatContainer(10, 1, 1, 0, 1, 1);

    public EnemyEntity create(Game game, UUID uuid, Vector2 pos, Map<String, Object> customData) {
        EnemyEntity enemy = new EnemyEntity(game, uuid, pos.cpy(), scale);
/*        enemy.centerPos = new Vector2();
        enemy.colliderRect = new Rectangle();
        enemy.colliderRect.setPosition(pos);
        enemy.colliderRect.setSize(scale());
        enemy.collidingWith = new HashSet<>();
        enemy.spawnTime = game.getGametime();
        enemy.getCenterPos();
        enemy.postSpawn();*/
        return enemy;
    }
}
