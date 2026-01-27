package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.World;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.provider.numberprovider.ConstantNumberProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;
import dev.creoii.chaos.util.stat.StatContainer;

import java.util.Map;

public record ObjectEntityType(String id, float scale, String lootTableId, StatContainer stats, NumberProvider experience) implements EntityType<ObjectEntity> {
    public static final MapCodec<ObjectEntityType> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("id").forGetter(ObjectEntityType::id),
            Codec.FLOAT.fieldOf("scale").orElse(1f).forGetter(ObjectEntityType::scale),
            Codec.STRING.fieldOf("loot_table").orElse("").forGetter(ObjectEntityType::lootTableId),
            StatContainer.INT_CODEC.fieldOf("stats").orElse(EnemyEntityType.DEFAULT_STAT_CONTAINER).forGetter(ObjectEntityType::stats),
            NumberProvider.CODEC.fieldOf("experience").orElse(ConstantNumberProvider.ZERO).forGetter(ObjectEntityType::experience)
        ).apply(instance, ObjectEntityType::new);
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
    public ObjectEntity create(World world, int id, Vector2 pos, Map<String, Object> customData) {
        return new ObjectEntity(world, this, id, pos.cpy());
    }
}
