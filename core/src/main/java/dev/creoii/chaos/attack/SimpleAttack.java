package dev.creoii.chaos.attack;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.util.provider.Provider;

import java.util.HashMap;
import java.util.Map;

public record SimpleAttack(String bulletId, Provider<Integer> damage, int bulletCount, int arcGap, float predictability, float angleOffset, Vector2 posOffset) implements Attack {
    public void attack(Target target, Entity entity) {
        Vector2 direction = target.getDirection(this, entity);
        float baseAngle = -arcGap * (bulletCount - 1) / 2f;

        for (int i = 0; i < bulletCount; i++) {
            float angle = baseAngle + i * arcGap;

            Map<String, Object> customData = new HashMap<>();
            customData.put("direction", direction.cpy().rotateDeg(angle + angleOffset));
            if (entity instanceof LivingEntity livingEntity)
                customData.put("damage", Math.round(damage.get(entity.getGame()) * .5f + livingEntity.getStats().attack.value() / 50f));

            BulletEntity bulletTemplate = entity.getGame().getDataManager().getBullet(bulletId);
            if (bulletTemplate != null) {
                BulletEntity bullet = entity.getGame().getEntityManager().addEntity(bulletTemplate, new Vector2(entity.getPos()).add(posOffset), customData);
                bullet.setParentGroup(entity.getGroup());
                bullet.setIndex(i % 2 == 0 ? 1 : -1);
            }
        }
    }
}
