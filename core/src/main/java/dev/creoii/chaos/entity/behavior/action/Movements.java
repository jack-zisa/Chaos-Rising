package dev.creoii.chaos.entity.behavior.action;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.entity.ServerEntity;
import dev.creoii.chaos.entity.ServerLivingEntity;
import dev.creoii.chaos.util.function.TriConsumer;

import java.util.Map;

public class Movements {
    public static final Map<String, TriConsumer<ServerEntity, Float, JsonValue>> MOVEMENTS = Map.of(
        "random", Movements::random,
        "chase", Movements::chase
    );

    public static void random(ServerEntity entity, float dt, JsonValue data) {
        float speed = (entity instanceof ServerLivingEntity living ? living.getStats().speed.value() : 1f) * data.getFloat("speed", 1f);
        entity.getPos().x += Math.random() < .5f ? -1f : 1f * speed * ServerEntity.COORDINATE_SCALE * dt;
        entity.getPos().y += Math.random() < .5f ? -1f : 1f * speed * ServerEntity.COORDINATE_SCALE * dt;
    }

    public static void chase(ServerEntity entity, float dt, JsonValue data) {
        /*float speed = (entity instanceof ServerLivingEntity living ? living.getStats().speed.value() : 1f) * data.getFloat("speed", 1f);
        Vector2 direction = new Vector2(entity.getGame().getActiveCharacter().getCenterPos()).sub(entity.getCenterPos()).nor();
        entity.getPos().add(direction.nor().scl(speed * ServerEntity.COORDINATE_SCALE * dt));*/
    }
}
