package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.World;
import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.provider.booleanprovider.BooleanProvider;

import java.util.Map;

public record LootDropEntityType(String id, float scale, BooleanProvider removeEmpty) implements EntityType<LootDropEntity> {
    public static final MapCodec<LootDropEntityType> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("id").forGetter(LootDropEntityType::id),
            Codec.FLOAT.fieldOf("scale").orElse(1f).forGetter(LootDropEntityType::scale),
            BooleanProvider.CODEC.fieldOf("remove_empty").forGetter(LootDropEntityType::removeEmpty)
        ).apply(instance, (id, scale, removeEmpty) -> new LootDropEntityType(id, scale, (BooleanProvider) removeEmpty.optimize()));
    });

    @Override
    public EntityGroup group() {
        return EntityGroup.LOOT_DROP;
    }

    @Override
    public float scale() {
        return scale * Entity.COORDINATE_SCALE;
    }

    @Override
    public LootDropEntity create(World world, int id, Vector2 pos, Map<String, Object> customData) {
        return new LootDropEntity(world, this, id, pos, new Inventory(2, 4));
    }
}
