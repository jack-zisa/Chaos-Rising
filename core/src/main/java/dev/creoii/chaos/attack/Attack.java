package dev.creoii.chaos.attack;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public record Attack(String bulletId, int damage, int bulletCount, int arcGap) {
    public static Attack parse(JsonValue jsonValue) {
        String bulletId = jsonValue.getString("bullet_id");
        int damage = jsonValue.getInt("damage", 0);
        int bulletCount = jsonValue.getInt("bullet_count", 1);
        int arcGap = jsonValue.getInt("arc_gap", 0);
        return new Attack(bulletId, damage, bulletCount, arcGap);
    }

    public void attack(Target target, Entity entity) {
        Vector2 direction = target.getDirection(entity);
        float baseAngle = -arcGap * (bulletCount - 1) / 2f;

        for (int i = 0; i < bulletCount; i++) {
            float angle = baseAngle + i * arcGap;

            Map<String, Object> customData = new HashMap<>();
            customData.put("direction", direction.cpy().rotateDeg(angle));
            customData.put("damage", damage);

            BulletEntity bullet = entity.getGame().getEntityManager().addEntity(entity.getGame().getDataManager().getBullet(bulletId), new Vector2(entity.getPos()), customData);
            bullet.setParentId(entity.getUuid());
            bullet.setIndex(i % 2 == 0 ? 1 : -1);
        }
    }

    public enum Target {
        MOUSE_POS(entity -> {
            Vector3 mousePos = entity.getGame().getInputManager().getMousePos();
            return new Vector2(mousePos.x, mousePos.y).sub(entity.getCenterPos()).nor();
        }),
        PLAYER(entity -> new Vector2(entity.getGame().getActiveCharacter().getCenterPos()).sub(entity.getCenterPos()).nor());

        private final Function<Entity, Vector2> direction;

        Target(Function<Entity, Vector2> direction) {
            this.direction = direction;
        }

        public Vector2 getDirection(Entity source) {
            return direction.apply(source);
        }
    }

    public static class Serializer implements Json.Serializer<Attack> {
        @Override
        public void write(Json json, Attack attack, Class knownType) {
            json.writeObjectStart();
            json.writeValue("bullet_id", attack.bulletId);
            json.writeValue("damage", attack.damage);
            json.writeValue("bullet_count", attack.bulletCount);
            json.writeValue("arc_gap", attack.arcGap);
            json.writeObjectEnd();
        }

        @Override
        public Attack read(Json json, JsonValue jsonValue, Class aClass) {
            return Attack.parse(jsonValue);
        }
    }
}
