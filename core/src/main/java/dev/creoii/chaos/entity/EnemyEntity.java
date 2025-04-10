package dev.creoii.chaos.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.entity.behavior.phase.Phase;
import dev.creoii.chaos.entity.behavior.phase.PhaseKey;
import dev.creoii.chaos.entity.behavior.transition.Transition;
import dev.creoii.chaos.entity.behavior.transition.Transitions;
import dev.creoii.chaos.entity.controller.EnemyController;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.texture.TextureManager;
import dev.creoii.chaos.util.stat.Stats;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class EnemyEntity extends LivingEntity implements DataManager.Identifiable {
    private final String id;
    private final EnemyController controller;

    public EnemyEntity(String id, String textureId, float scale, EnemyController controller) {
        super(textureId, scale, new Vector2(1, 1), Group.ENEMY, new Stats(), new Stats());
        this.id = id;
        this.controller = controller;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void onLoad(Main main) {
        if (main.getGame().getCollisionManager().getCellSize() < getScale())
            main.getGame().getCollisionManager().setCellSize(getScale());
    }

    @Override
    public void collide(Entity other) {
        if (other.getGroup() == Group.CHARACTER) {
            ((LivingEntity) other).damage(5);
        }
    }

    @Override
    public void postSpawn() {
        if (controller != null)
            controller.start(this);
    }

    @Override
    public void tick(int gametime, float delta) {
        super.tick(gametime, delta);

        if (getStats().health <= 0)
            remove();
    }

    @Override
    public Entity create(Game game, UUID uuid, Vector2 pos) {
        EnemyEntity entity = new EnemyEntity(id, getTextureId(), getScale() / COORDINATE_SCALE, controller);
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
            // write phases
            json.writeObjectEnd();
        }

        @Override
        public EnemyEntity read(Json json, JsonValue jsonValue, Class aClass) {
            String id = jsonValue.getString("id");
            String spritePath = jsonValue.getString("texture", TextureManager.DEFAULT_TEXTURE_ID);
            float scale = jsonValue.getFloat("scale", 1f);

            if (jsonValue.has("controller")) {
                JsonValue controller = jsonValue.get("controller");
                String startPhaseKey = controller.getString("start_phase");

                Map<PhaseKey, Phase> phases = new LinkedHashMap<>();
                int i = 0;
                for (JsonValue jsonValue1 : controller.get("phases")) {
                    JsonValue transition = jsonValue1.get("transition");
                    Phase phase = new Phase(jsonValue1.name, jsonValue1.getInt("duration"), Transition.parse(transition));
                    phases.put(new PhaseKey(jsonValue1.name, i), phase);
                    ++i;
                }
                return new EnemyEntity(id, spritePath, scale, new EnemyController(phases, startPhaseKey));
            }
            return new EnemyEntity(id, spritePath, scale, null);
        }
    }
}
