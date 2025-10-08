package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.behavior.Behavior;
import dev.creoii.chaos.loot.LootTable;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.stat.StatContainer;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public record EnemyEntityType(String id, float scale, @Nullable LootTable lootTable, @Nullable Behavior behavior, StatContainer stats) implements EntityType<EnemyEntity> {
    public static final StatContainer DEFAULT_STAT_CONTAINER = new StatContainer(10, 1, 1, 0, 1, 1);
    public static final MapCodec<EnemyEntityType> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("id").forGetter(EnemyEntityType::id),
            Codec.FLOAT.fieldOf("scale").orElse(1f).forGetter(EnemyEntityType::scale),
            LootTable.CODEC.fieldOf("loot_table").orElse(LootTable.EMPTY).forGetter(EnemyEntityType::lootTable),
            StatContainer.CODEC.fieldOf("stats").orElse(DEFAULT_STAT_CONTAINER).forGetter(EnemyEntityType::stats)
        ).apply(instance, (id, scale, lootTable, stats) -> new EnemyEntityType(id, scale, lootTable, null, stats));
    });

    @Override
    public EntityGroup group() {
        return EntityGroup.ENEMY;
    }

    @Override
    public float scale() {
        return scale * Entity.COORDINATE_SCALE;
    }

    public EnemyEntity create(Game game, UUID uuid, Vector2 pos, Map<String, Object> customData) {
        EnemyEntity enemy = new EnemyEntity(game, this, uuid, pos.cpy());
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
