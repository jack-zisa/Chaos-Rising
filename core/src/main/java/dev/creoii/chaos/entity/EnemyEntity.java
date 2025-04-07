package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.ai.controller.EntityController;
import dev.creoii.chaos.util.stat.Stats;

import java.util.UUID;

public class EnemyEntity extends LivingEntity implements DataManager.Identifiable {
    private final String id;
    private final float scale;

    public EnemyEntity(String id, float scale) {
        super(new Vector2(32, 32).scl(scale), Group.ENEMY, new Stats(), new Stats());
        this.id = id;
        this.scale = scale;
    }

    @Override
    public String getId() {
        return id;
    }

    public float getScale() {
        return scale;
    }

    @Override
    public void collide(LivingEntity other) {
        if (other.getGroup() == Group.CHARACTER) {
            other.damage(5);
        }
    }

    @Override
    public Entity create(Game game, UUID uuid, Vector2 pos) {
        return new EnemyEntity(id, scale);
    }

    @Override
    public EntityController<?> getController() {
        return null;
    }

    public static class Serializer implements Json.Serializer<EnemyEntity> {
        @Override
        public void write(Json json, EnemyEntity enemy, Class knownType) {
            json.writeObjectStart();
            json.writeValue("id", enemy.getId());
            json.writeValue("scale", enemy.getScale());
            json.writeObjectEnd();
        }

        @Override
        public EnemyEntity read(Json json, JsonValue jsonValue, Class aClass) {
            String id = jsonValue.getString("id");
            float scale = jsonValue.getFloat("scale", 1f);
            return new EnemyEntity(id, scale);
        }
    }
}
