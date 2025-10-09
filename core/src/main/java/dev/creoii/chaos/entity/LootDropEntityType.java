package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.provider.booleanprovider.BooleanProvider;

import java.util.Map;
import java.util.UUID;

public record LootDropEntityType(String id, float scale, BooleanProvider removeEmpty) implements EntityType<LootDropEntity> {
    public static final MapCodec<LootDropEntityType> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("id").forGetter(LootDropEntityType::id),
            Codec.FLOAT.fieldOf("scale").orElse(1f).forGetter(LootDropEntityType::scale),
            BooleanProvider.CODEC.fieldOf("remove_empty").forGetter(LootDropEntityType::removeEmpty)
        ).apply(instance, LootDropEntityType::new);
    });

    @Override
    public EntityGroup group() {
        return EntityGroup.LOOT_DROP;
    }

    @Override
    public float scale() {
        return scale * Entity.COORDINATE_SCALE;
    }

    public LootDropEntity create(Game game, UUID uuid, Vector2 pos, Map<String, Object> customData) {
        LootDropEntity lootDrop = new LootDropEntity(game, this, uuid, pos, new Inventory(2, 4));
        /*lootDrop.centerPos = new Vector2();
        lootDrop.colliderRect = new Rectangle();
        lootDrop.colliderRect.setPosition(pos);
        lootDrop.colliderRect.setSize(scale());
        lootDrop.collidingWith = new HashSet<>();
        lootDrop.spawnTime = game.getGametime();
        lootDrop.getCenterPos();
        lootDrop.postSpawn();*/
        return lootDrop;
    }
}
