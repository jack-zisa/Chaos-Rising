package dev.creoii.chaos.attack;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.character.CharacterEntity;
import dev.creoii.chaos.util.provider.IntProvider;
import dev.creoii.chaos.util.provider.Provider;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

public interface Attack {
    void attack(Target target, Entity entity);

    static Attack parse(JsonValue jsonValue) {
        if (jsonValue.has("attacks")) {
            JsonValue segmentsList = jsonValue.get("attacks");
            Set<Attack> attacks = new HashSet<>();
            segmentsList.forEach(jsonValue1 -> attacks.add(parse(jsonValue1)));
            return new MultiAttack(attacks);
        } else {
            String bulletId = jsonValue.getString("bullet_id");
            Provider<Integer> damage = IntProvider.parse(jsonValue.get("damage"), 0);
            int bulletCount = jsonValue.getInt("bullet_count", 1);
            int arcGap = jsonValue.getInt("arc_gap", 0);
            float predictability = jsonValue.getFloat("predictability", 0f);
            float angleOffset = jsonValue.getFloat("angle_offset", 0f);

            Vector2 posOffsetVec;
            if (jsonValue.has("pos_offset")) {
                JsonValue posOffsetValue = jsonValue.get("pos_offset");
                posOffsetVec = new Vector2(posOffsetValue.get(0).asInt(), posOffsetValue.get(1).asInt()).scl(Entity.COORDINATE_SCALE);
            } else {
                posOffsetVec = Vector2.Zero;
            }
            return new SimpleAttack(bulletId, damage, bulletCount, arcGap, predictability, angleOffset, posOffsetVec);
        }
    }

    enum Target {
        MOUSE_POS((attack, entity) -> {
            Vector3 mousePos = entity.getGame().getInputManager().getMousePos();
            return new Vector2(mousePos.x, mousePos.y).sub(entity.getCenterPos()).nor();
        }),
        PLAYER((attack, entity) -> {
            CharacterEntity character = entity.getGame().getActiveCharacter();

            Vector2 pos = character.getCenterPos();
            Vector2 velocity = pos.cpy().sub(character.getPrevCenterPos());

            Vector2 predicted = pos.cpy().add(velocity.scl(attack.predictability()));
            return predicted.sub(entity.getCenterPos()).nor();
        });

        private final BiFunction<SimpleAttack, Entity, Vector2> direction;

        Target(BiFunction<SimpleAttack, Entity, Vector2> direction) {
            this.direction = direction;
        }

        public Vector2 getDirection(SimpleAttack attack, Entity source) {
            return direction.apply(attack, source);
        }
    }
}
