package dev.creoii.chaos.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.ServerGame;
import dev.creoii.chaos.entity.behavior.Behavior;
import dev.creoii.chaos.loot.LootTable;
import dev.creoii.chaos.texture.TextureManager;
import dev.creoii.chaos.util.stat.StatContainer;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public record EnemyEntityType(String id, float scale, @Nullable String textureId, @Nullable LootTable lootTable, @Nullable Behavior behavior, StatContainer statContainer) implements EntityType<EnemyEntity> {
    public static final StatContainer DEFAULT_STAT_CONTAINER = new StatContainer(10, 1, 1, 0, 1, 1);

    @Override
    public void onLoad(Main main) {
        if (main.getGame().getCollisionManager().getCellSize() < scale())
            main.getGame().getCollisionManager().setCellSize(scale());
    }

    @Override
    public float scale() {
        return scale * Entity.COORDINATE_SCALE;
    }

    public EnemyEntity create(ServerGame game, Vector2 pos, Map<String, Object> customData) {
        EnemyEntity enemy = new EnemyEntity(pos.cpy(), this);
        enemy.game = game;
        enemy.uuid = UUID.randomUUID();
        enemy.pos = pos;
        enemy.centerPos = new Vector2();
        enemy.colliderRect = new Rectangle();
        enemy.colliderRect.setPosition(pos);
        enemy.colliderRect.setWidth(enemy.getCollider().x * scale());
        enemy.colliderRect.setHeight(enemy.getCollider().y * scale());
        enemy.collidingWith = new HashSet<>();
        enemy.spawnTime = game.getGametime();
        if (textureId != null) {
            enemy.sprite = new Sprite(game.getTextureManager().getTexture("enemy", textureId));
            enemy.sprite.setSize(scale(), scale());
            enemy.getCenterPos();
        }
        enemy.postSpawn();
        return enemy;
    }

    public static EnemyEntityType parse(String id, JsonValue jsonValue) {
        String textureId = jsonValue.getString("texture", TextureManager.DEFAULT_TEXTURE_ID);
        float scale = jsonValue.getFloat("scale", 1f);
        StatContainer statContainer = jsonValue.has("stats") ? StatContainer.parse(jsonValue.get("stats")) : DEFAULT_STAT_CONTAINER.copy();
        LootTable lootTable = jsonValue.has("loot_table") ? LootTable.parse(jsonValue.get("loot_table")) : null;
        if (jsonValue.has("behavior")) {
            Behavior behavior = Behavior.parse(jsonValue.get("behavior"));
            return new EnemyEntityType(id, scale, textureId, lootTable, behavior, statContainer);
        }
        return new EnemyEntityType(id, scale, textureId, lootTable, null, statContainer);
    }
}
