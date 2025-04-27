package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.util.provider.booleanprovider.BooleanProvider;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public record LootDropEntityType(String id, float scale, @Nullable String textureId, BooleanProvider removeEmpty) implements EntityType<LootDropEntity> {
    public LootDropEntity create(Game game, UUID uuid, Vector2 pos, Map<String, Object> customData) {
        LootDropEntity lootDrop = new LootDropEntity(game, uuid, pos, scale, new Inventory(2, 4));
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
