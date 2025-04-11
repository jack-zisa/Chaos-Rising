package dev.creoii.chaos.attack;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.entity.character.CharacterEntity;
import dev.creoii.chaos.util.provider.IntProvider;
import dev.creoii.chaos.util.provider.Provider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public record Attack(String bulletId, Provider<Integer> damage, int bulletCount, int arcGap, float predictability) {
    public static Attack parse(JsonValue jsonValue) {
        String bulletId = jsonValue.getString("bullet_id");
        Provider<Integer> damage = IntProvider.parse(jsonValue.get("damage"), 0);
        int bulletCount = jsonValue.getInt("bullet_count", 1);
        int arcGap = jsonValue.getInt("arc_gap", 0);
        float predictability = jsonValue.getFloat("predictability", 0f);
        return new Attack(bulletId, damage, bulletCount, arcGap, predictability);
    }

    public void attack(Target target, Entity entity) {
        Vector2 direction = target.getDirection(this, entity);
        float baseAngle = -arcGap * (bulletCount - 1) / 2f;

        for (int i = 0; i < bulletCount; i++) {
            float angle = baseAngle + i * arcGap;

            Map<String, Object> customData = new HashMap<>();
            customData.put("direction", direction.cpy().rotateDeg(angle));
            if (entity instanceof LivingEntity livingEntity)
                customData.put("damage", Math.round(damage.get(entity.getGame()) * .5f + livingEntity.getStats().attack.value() / 50f));

            BulletEntity bullet = entity.getGame().getEntityManager().addEntity(entity.getGame().getDataManager().getBullet(bulletId), new Vector2(entity.getPos()), customData);
            bullet.setParentGroup(entity.getGroup());
            bullet.setIndex(i % 2 == 0 ? 1 : -1);
        }
    }

    public enum Target {
        MOUSE_POS((attack, entity) -> {
            Vector3 mousePos = entity.getGame().getInputManager().getMousePos();
            return new Vector2(mousePos.x, mousePos.y).sub(entity.getCenterPos()).nor();
        }),
        PLAYER((attack, entity) -> {
            CharacterEntity character = entity.getGame().getActiveCharacter();

            Vector2 pos = character.getCenterPos();
            Vector2 velocity = pos.cpy().sub(character.getPrevCenterPos());

            Vector2 predicted = pos.cpy().add(velocity.scl(attack.predictability));
            return predicted.sub(entity.getCenterPos()).nor();
        });

        private final BiFunction<Attack, Entity, Vector2> direction;

        Target(BiFunction<Attack, Entity, Vector2> direction) {
            this.direction = direction;
        }

        public Vector2 getDirection(Attack attack, Entity source) {
            return direction.apply(attack, source);
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
