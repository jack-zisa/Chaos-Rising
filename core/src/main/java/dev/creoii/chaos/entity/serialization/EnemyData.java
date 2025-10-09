package dev.creoii.chaos.entity.serialization;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.stat.StatContainer;

public record EnemyData(StatContainer baseStats, StatContainer maxStats) implements EntityCustomData {
    public static final MapCodec<EnemyData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        StatContainer.STAT_CODEC.fieldOf("base_stats").forGetter(EnemyData::baseStats),
        StatContainer.STAT_CODEC.fieldOf("max_stats").forGetter(EnemyData::maxStats)
    ).apply(instance, EnemyData::new));

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.ENEMY;
    }

    @Override
    public MapCodec<EnemyData> getCodec() {
        return CODEC;
    }
}
