package dev.creoii.chaos.entity.ai.movement;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.util.TriConsumer;

import java.util.Map;

public class Movements {
    public static final Map<String, TriConsumer<Entity, Float, Map<String, Object>>> BEHAVIORS = Map.of(
        "random", Movements::random,
        "chase", Movements::chase
    );

    public static void random(Entity entity, float dt, Map<String, Object> data) {
        float speed = (entity instanceof LivingEntity living ? living.getStats().speed : 1f) * (float) data.getOrDefault("speed", 1f);

        float dx = Math.random() < 0.5 ? -1 : 1;
        float dy = Math.random() < 0.5 ? -1 : 1;

        entity.getPos().x += dx * speed * Entity.COORDINATE_SCALE * dt;
        entity.getPos().y += dy * speed * Entity.COORDINATE_SCALE * dt;
    }

    public static void chase(Entity entity, float dt, Map<String, Object> data) {
        float speed = (entity instanceof LivingEntity living ? living.getStats().speed : 1f) * (float) data.getOrDefault("speed", 1f);
        Vector2 direction = new Vector2(entity.getGame().getActiveCharacter().getCenterPos()).sub(entity.getCenterPos()).nor();
        entity.getPos().add(direction.nor().scl(speed * Entity.COORDINATE_SCALE * dt));
    }
}
