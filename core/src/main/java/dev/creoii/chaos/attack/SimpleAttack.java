package dev.creoii.chaos.attack;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.entity.BulletEntityType;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record SimpleAttack(String bulletId, NumberProvider damage, int bulletCount, int arcGap, float predictability, NumberProvider angleOffset, VecProvider source, VecProvider target) implements Attack, Serializable {
    public void attack(VecProvider targetPos, VecProvider sourcePos, Entity sourceEntity) {
        Provider.Context context = Provider.Context.of(sourceEntity, sourceEntity.getGame().getGametime());
        Vector2 pos = source != null ? source.get(context) : sourcePos.get(context);
        Vector2 direction = target != null ? target.get(context).sub(pos).nor() : targetPos.get(context).sub(pos).nor();
        float baseAngle = -arcGap * (bulletCount - 1) / 2f;

        for (int i = 0; i < bulletCount; ++i) {
            float angle = baseAngle + i * arcGap;

            Map<String, Object> customData = new HashMap<>();
            customData.put("direction", direction.cpy().rotateDeg(angle + angleOffset.get(context)));
            customData.put("index", i % 2 == 0 ? 1 : -1);
            if (sourceEntity instanceof LivingEntity livingEntity)
                customData.put("damage", Math.round(damage.getInt(context) * .5f + livingEntity.getStats().attack().value() / 50f));

            BulletEntityType bulletType = DataManager.getBullet(bulletId);
            if (bulletType != null) {
                BulletEntity bullet = sourceEntity.getGame().getEntityManager().addEntity(UUID.randomUUID(), bulletType, pos.cpy(), customData);
                bullet.setParent(sourceEntity);
            }
        }
    }
}
