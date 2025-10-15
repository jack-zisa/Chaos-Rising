package dev.creoii.chaos.entity.serialization;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.EnemyEntityType;
import dev.creoii.chaos.network.PacketUtils;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.stat.StatContainer;

import java.util.Optional;

public record EnemyData(StatContainer baseStats, StatContainer maxStats) implements EntityCustomData {
    public static final MapCodec<EnemyData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        StatContainer.INT_CODEC.optionalFieldOf("base_stats").forGetter(enemyData -> enemyData.baseStats.equals(EnemyEntityType.DEFAULT_STAT_CONTAINER) ? Optional.empty() : Optional.of(enemyData.baseStats)),
        StatContainer.INT_CODEC.optionalFieldOf("max_stats").forGetter(enemyData -> enemyData.maxStats.equals(enemyData.baseStats) ? Optional.empty() : Optional.of(enemyData.maxStats))
    ).apply(instance, (baseStats, maxStats) -> new EnemyData(baseStats.orElse(EnemyEntityType.DEFAULT_STAT_CONTAINER), maxStats.orElse(baseStats.orElse(EnemyEntityType.DEFAULT_STAT_CONTAINER)))));

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.ENEMY;
    }

    @Override
    public MapCodec<EnemyData> getCodec() {
        return CODEC;
    }

    @Override
    public void write(Output output) {
        PacketUtils.writeStatContainer(output, baseStats);
        PacketUtils.writeStatContainer(output, maxStats);
    }

    public static EnemyData read(Input input) {
        return new EnemyData(PacketUtils.readStatContainer(input), PacketUtils.readStatContainer(input));
    }
}
