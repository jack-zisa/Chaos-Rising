package dev.creoii.chaos.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.entity.behavior.Behavior;
import dev.creoii.chaos.entity.controller.EnemyController;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.loot.LootTable;
import dev.creoii.chaos.texture.TextureManager;
import dev.creoii.chaos.util.LootUtils;
import dev.creoii.chaos.util.stat.StatContainer;

import javax.annotation.Nullable;
import java.util.UUID;

public class EnemyEntity extends LivingEntity implements DataManager.Identifiable {
    private String id;
    private final EnemyController controller;
    @Nullable
    private final LootTable lootTable;

    public EnemyEntity(String textureId, float scale, EnemyController controller, @Nullable LootTable lootTable, StatContainer statContainer) {
        super(textureId, scale, new Vector2(1, 1), Group.ENEMY, statContainer.copy(), statContainer.copy());
        this.controller = controller;
        this.lootTable = lootTable;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Nullable
    public LootTable getLootTable() {
        return lootTable;
    }

    @Override
    public void onLoad(Main main) {
        if (main.getGame().getCollisionManager().getCellSize() < getScale())
            main.getGame().getCollisionManager().setCellSize(getScale());
    }

    @Override
    public void collisionEnter(Entity other) {

    }

    @Override
    public void collisionExit(Entity other) {

    }

    @Override
    public void postSpawn() {
        if (controller != null)
            controller.start(this);
    }

    @Override
    public void onDeath() {
        if (lootTable != null) {
            int rolls = Entity.RANDOM.nextInt(4);
            if (rolls == 0)
                return;
            LootDropEntity lootDropEntity = new LootDropEntity("bag", 1f);
            game.getEntityManager().addEntity(lootDropEntity, pos.cpy());
            LootUtils.insertIntoInventory(getGame(), lootDropEntity.getInventory(), lootTable, rolls);
        }
    }

    @Override
    public void tick(int gametime, float delta) {
        super.tick(gametime, delta);

        if (getStats().health.value() <= 0)
            remove();
    }

    @Override
    public Entity create(Game game, UUID uuid, Vector2 pos) {
        EnemyEntity entity = new EnemyEntity(getTextureId(), getScale() / COORDINATE_SCALE, controller == null ? null : new EnemyController(controller), lootTable, getMaxStats().copy());
        entity.setId(id);
        entity.sprite = new Sprite(game.getTextureManager().getTexture("enemy", getTextureId()));
        entity.sprite.setSize(getScale(), getScale());
        entity.setMoving(true);
        return entity;
    }

    @Override
    public EntityController<EnemyEntity> getController() {
        return controller;
    }

    public static class Serializer implements Json.Serializer<EnemyEntity> {
        @Override
        public void write(Json json, EnemyEntity enemy, Class knownType) {
            json.writeObjectStart();
            json.writeValue("id", enemy.id());
            json.writeValue("scale", enemy.getScale());
            json.writeValue("texture", enemy.getTextureId());
            json.writeValue("base_stats", enemy.getMaxStats());
            json.writeValue("max_stats", enemy.getMaxStats());
            // write phases
            json.writeObjectEnd();
        }

        @Override
        public EnemyEntity read(Json json, JsonValue jsonValue, Class aClass) {
            String spritePath = jsonValue.getString("texture", TextureManager.DEFAULT_TEXTURE_ID);
            float scale = jsonValue.getFloat("scale", 1f);
            StatContainer statContainer = jsonValue.has("stats") ? json.readValue(StatContainer.class, jsonValue.get("stats")) : DEFAULT_STAT_CONTAINER.copy();
            LootTable lootTable = jsonValue.has("loot_table") ? LootTable.parse(jsonValue.get("loot_table")) : null;
            if (jsonValue.has("controller")) {
                JsonValue controller = jsonValue.get("controller");
                Behavior behavior = Behavior.parse(controller.get("behavior"));
                return new EnemyEntity(spritePath, scale, new EnemyController(behavior), lootTable, statContainer);
            }
            return new EnemyEntity(spritePath, scale, null, lootTable, statContainer);
        }
    }
}
