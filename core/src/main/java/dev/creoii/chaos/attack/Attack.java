package dev.creoii.chaos.attack;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;
import dev.creoii.chaos.util.provider.vecprovider.SourcePosVecProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

import java.util.HashSet;
import java.util.Set;

public interface Attack {
    void attack(VecProvider targetPos, VecProvider sourcePos, Entity sourceEntity);

    default void attack(VecProvider targetPos, Entity sourceEntity) {
        attack(targetPos, new SourcePosVecProvider(), sourceEntity);
    }

    static Attack parse(JsonValue jsonValue) {
        if (jsonValue.has("attacks")) {
            JsonValue segments = jsonValue.get("attacks");
            Set<Attack> attacks = new HashSet<>();
            segments.forEach(jsonValue1 -> attacks.add(parse(jsonValue1)));
            return new MultiAttack(attacks);
        } else {
            String bulletId = jsonValue.getString("bullet_id");
            NumberProvider damage = NumberProvider.parse(jsonValue.get("damage"));
            int bulletCount = jsonValue.getInt("bullet_count", 1);
            int arcGap = jsonValue.getInt("arc_gap", 0);
            float predictability = jsonValue.getFloat("predictability", 0f);
            NumberProvider angleOffset = NumberProvider.parse(jsonValue.get("angle_offset"), 0f);

            VecProvider target = null;
            if (jsonValue.has("target")) {
                target = VecProvider.parse(jsonValue.get("target"));
            }

            VecProvider source = null;
            if (jsonValue.has("source")) {
                source = VecProvider.parse(jsonValue.get("source"));
            }

            return new SimpleAttack(bulletId, damage, bulletCount, arcGap, predictability, angleOffset, source, target);
        }
    }
}
