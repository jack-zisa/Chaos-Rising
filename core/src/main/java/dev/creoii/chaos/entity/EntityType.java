package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.Codec;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.Identifiable;

import java.util.Map;
import java.util.UUID;

public interface EntityType<T extends Entity> extends Identifiable {
    Codec<EntityType<?>> CODEC = EntityGroup.CODEC.dispatch(EntityType::group, group -> switch (group) {
        case CHARACTER -> CharacterEntityType.CODEC;
        case ENEMY     -> EnemyEntityType.CODEC;
        case BULLET    -> BulletEntityType.CODEC;
        case LOOT_DROP -> LootDropEntityType.CODEC;
    });

    String id();

    float scale();

    EntityGroup group();

    T create(Game game, UUID uuid, Vector2 pos, Map<String, Object> customData);
}
