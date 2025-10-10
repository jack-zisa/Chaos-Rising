package dev.creoii.chaos.entity.behavior.action;

import dev.creoii.chaos.entity.Entity;

import java.util.Map;
import java.util.function.BiConsumer;

public class Movements {
    public static final Map<String, BiConsumer<Entity, Float>> MOVEMENTS = Map.of(
        "random", Movements::random,
        "chase", Movements::chase
    );

    public static void random(Entity entity, float dt) {
        //float speed = (entity instanceof LivingEntity living ? living.getStats().speed().value() : 1f) * data.getFloat("speed", 1f);
        //entity.getPos().x += (Math.random() < .5f ? -1f : 1f) * speed * Entity.COORDINATE_SCALE * dt;
        //entity.getPos().y += (Math.random() < .5f ? -1f : 1f) * speed * Entity.COORDINATE_SCALE * dt;
    }

    public static void chase(Entity entity, float dt) {
        /*float speed = (entity instanceof LivingEntity living ? living.getStats().speed.value() : 1f) * data.getFloat("speed", 1f);
        Vector2 direction = new Vector2(entity.getGame().getActiveCharacter().getCenterPos()).sub(entity.getCenterPos()).nor();
        entity.getPos().add(direction.nor().scl(speed * Entity.COORDINATE_SCALE * dt));*/
    }
}
