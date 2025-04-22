package dev.creoii.chaos.attack;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.floatprovider.FloatProvider;
import dev.creoii.chaos.util.provider.intprovider.IntProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

import java.util.HashMap;
import java.util.Map;

public record SimpleAttack(String bulletId, IntProvider damage, int bulletCount, int arcGap, float predictability, FloatProvider angleOffset, VecProvider source, VecProvider target) implements Attack {
    public void attack(VecProvider targetPos, VecProvider sourcePos, Entity sourceEntity) {
        Provider.Context context = Provider.Context.of(sourceEntity, sourceEntity.getGame().getGametime());
        Vector2 pos = source != null ? source.get(context) : sourcePos.get(context);
        Vector2 direction = target != null ? target.get(context).sub(pos).nor() : targetPos.get(context).sub(pos).nor();
        float baseAngle = -arcGap * (bulletCount - 1) / 2f;

        for (int i = 0; i < bulletCount; ++i) {
            float angle = baseAngle + i * arcGap;

            Map<String, Object> customData = new HashMap<>();
            customData.put("direction", direction.cpy().rotateDeg(angle + angleOffset.get(context)));
            if (sourceEntity instanceof LivingEntity livingEntity)
                customData.put("damage", Math.round(damage.get(context) * .5f + livingEntity.getStats().attack.value() / 50f));

            BulletEntity bulletTemplate = sourceEntity.getGame().getDataManager().getBullet(bulletId);
            if (bulletTemplate != null) {
                BulletEntity bullet = sourceEntity.getGame().getEntityManager().addEntity(bulletTemplate, pos.cpy(), customData);
                bullet.setParent(sourceEntity);
                bullet.setIndex(i % 2 == 0 ? 1 : -1);
            }
        }
    }
}
