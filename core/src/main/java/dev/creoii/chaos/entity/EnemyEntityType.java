package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.behavior.Behavior;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.stat.StatContainer;

import javax.annotation.Nullable;
import java.util.Map;

public record EnemyEntityType(String id, float scale, String lootTableId, @Nullable Behavior behavior, StatContainer stats) implements EntityType<EnemyEntity> {
    public static final StatContainer DEFAULT_STAT_CONTAINER = new StatContainer(10, 1, 1, 0, 1, 1);
    public static final MapCodec<EnemyEntityType> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("id").forGetter(EnemyEntityType::id),
            Codec.FLOAT.fieldOf("scale").orElse(1f).forGetter(EnemyEntityType::scale),
            Codec.STRING.fieldOf("loot_table").orElse("").forGetter(EnemyEntityType::lootTableId),
            Behavior.CODEC.fieldOf("behavior").forGetter(EnemyEntityType::behavior),
            StatContainer.INT_CODEC.fieldOf("stats").orElse(DEFAULT_STAT_CONTAINER).forGetter(EnemyEntityType::stats)
        ).apply(instance, EnemyEntityType::new);
    });

    @Override
    public EntityGroup group() {
        return EntityGroup.ENEMY;
    }

    @Override
    public float scale() {
        return scale * Entity.COORDINATE_SCALE;
    }

    @Override
    public EnemyEntity create(Game game, int id, Vector2 pos, Map<String, Object> customData) {
        return new EnemyEntity(game, this, id, pos.cpy());
    }
}
