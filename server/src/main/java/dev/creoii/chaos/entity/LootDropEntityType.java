package dev.creoii.chaos.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.ServerGame;
import dev.creoii.chaos.texture.TextureManager;
import dev.creoii.chaos.util.provider.booleanprovider.BooleanProvider;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public record LootDropEntityType(String id, float scale, @Nullable String textureId, BooleanProvider removeEmpty) implements EntityType<LootDropEntity> {
    @Override
    public void onLoad(Main main) {
        if (main.getGame().getCollisionManager().getCellSize() < scale())
            main.getGame().getCollisionManager().setCellSize(scale());
    }

    @Override
    public float scale() {
        return scale * Entity.COORDINATE_SCALE;
    }

    public LootDropEntity create(ServerGame game, Vector2 pos, Map<String, Object> customData) {
        LootDropEntity lootDrop = new LootDropEntity(this);
        lootDrop.game = game;
        lootDrop.uuid = UUID.randomUUID();
        lootDrop.pos = pos;
        lootDrop.centerPos = new Vector2();
        lootDrop.colliderRect = new Rectangle();
        lootDrop.colliderRect.setPosition(pos);
        lootDrop.colliderRect.setWidth(lootDrop.getCollider().x * scale());
        lootDrop.colliderRect.setHeight(lootDrop.getCollider().y * scale());
        lootDrop.collidingWith = new HashSet<>();
        lootDrop.spawnTime = game.getGametime();
        if (textureId != null) {
            lootDrop.sprite = new Sprite(game.getTextureManager().getTexture("loot_drop", textureId));
            lootDrop.sprite.setSize(scale(), scale());
            lootDrop.getCenterPos();
        }
        lootDrop.postSpawn();
        return lootDrop;
    }

    public static LootDropEntityType parse(String id, JsonValue jsonValue) {
        float scale = jsonValue.getFloat("scale", 1f);
        String textureId = jsonValue.getString("texture", TextureManager.DEFAULT_TEXTURE_ID);
        BooleanProvider removeEmpty = BooleanProvider.parse(jsonValue.get("remove_empty"), false);
        return new LootDropEntityType(id, scale, textureId, removeEmpty);
    }
}
