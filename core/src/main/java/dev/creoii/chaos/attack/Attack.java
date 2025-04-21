package dev.creoii.chaos.attack;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.util.provider.floatprovider.FloatProvider;
import dev.creoii.chaos.util.provider.intprovider.IntProvider;
import dev.creoii.chaos.util.provider.vecprovider.SourceVecProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

import java.util.HashSet;
import java.util.Set;

public interface Attack {
    void attack(VecProvider targetPos, VecProvider sourcePos, Entity sourceEntity);

    default void attack(VecProvider targetPos, Entity sourceEntity) {
        attack(targetPos, new SourceVecProvider(), sourceEntity);
    }

    static Attack parse(JsonValue jsonValue) {
        if (jsonValue.has("attacks")) {
            JsonValue segments = jsonValue.get("attacks");
            Set<Attack> attacks = new HashSet<>();
            segments.forEach(jsonValue1 -> attacks.add(parse(jsonValue1)));
            return new MultiAttack(attacks);
        } else {
            String bulletId = jsonValue.getString("bullet_id");
            IntProvider damage = IntProvider.parse(jsonValue.get("damage"));
            int bulletCount = jsonValue.getInt("bullet_count", 1);
            int arcGap = jsonValue.getInt("arc_gap", 0);
            float predictability = jsonValue.getFloat("predictability", 0f);
            FloatProvider angleOffset = FloatProvider.parse(jsonValue.get("angle_offset"), 0f);

            Vector2 posOffsetVec;
            if (jsonValue.has("pos_offset")) {
                JsonValue posOffsetValue = jsonValue.get("pos_offset");
                posOffsetVec = new Vector2(posOffsetValue.get(0).asInt(), posOffsetValue.get(1).asInt()).scl(Entity.COORDINATE_SCALE);
            } else {
                posOffsetVec = Vector2.Zero;
            }

            VecProvider target = null;
            if (jsonValue.has("target")) {
                target = VecProvider.parse(jsonValue.get("target"));
            }

            VecProvider source = null;
            if (jsonValue.has("source")) {
                source = VecProvider.parse(jsonValue.get("source"));
            }

            return new SimpleAttack(bulletId, damage, bulletCount, arcGap, predictability, angleOffset, posOffsetVec, source, target);
        }
    }
}
