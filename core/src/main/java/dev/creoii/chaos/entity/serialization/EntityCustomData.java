package dev.creoii.chaos.entity.serialization;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.util.EntityGroup;

public sealed interface EntityCustomData permits BulletData, CharacterData, EnemyData, LootDropData {
    Codec<EntityCustomData> CODEC = EntityGroup.CODEC.dispatch(EntityCustomData::getGroup, entityGroup -> switch (entityGroup) {
        case CHARACTER -> CharacterData.CODEC;
        case ENEMY -> EnemyData.CODEC;
        case BULLET -> BulletData.CODEC;
        case LOOT_DROP -> LootDropData.CODEC;
    });

    EntityGroup getGroup();

    MapCodec<? extends EntityCustomData> getCodec();
}
