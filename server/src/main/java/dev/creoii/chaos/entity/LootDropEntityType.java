package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.ServerGame;
import dev.creoii.chaos.util.provider.booleanprovider.BooleanProvider;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public record LootDropEntityType(String id, float scale, @Nullable String textureId, BooleanProvider removeEmpty) implements EntityType<ServerLootDropEntity> {
    public ServerLootDropEntity create(ServerGame game, UUID uuid, Vector2 pos, Map<String, Object> customData) {
        ServerLootDropEntity lootDrop = new ServerLootDropEntity(this);
        lootDrop.game = game;
        lootDrop.uuid = uuid;
        lootDrop.pos = pos;
        lootDrop.centerPos = new Vector2();
        lootDrop.colliderRect = new Rectangle();
        lootDrop.colliderRect.setPosition(pos);
        lootDrop.colliderRect.setSize(scale());
        lootDrop.collidingWith = new HashSet<>();
        lootDrop.spawnTime = game.getGametime();
        lootDrop.getCenterPos();
        lootDrop.postSpawn();
        return lootDrop;
    }
}
