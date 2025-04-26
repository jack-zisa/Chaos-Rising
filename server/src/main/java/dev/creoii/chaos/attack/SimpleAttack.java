package dev.creoii.chaos.attack;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.ServerBulletEntity;
import dev.creoii.chaos.entity.BulletEntityType;
import dev.creoii.chaos.entity.ServerEntity;
import dev.creoii.chaos.entity.ServerLivingEntity;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record SimpleAttack(String bulletId, NumberProvider damage, int bulletCount, int arcGap, float predictability, NumberProvider angleOffset, VecProvider source, VecProvider target) implements Attack {
    public void attack(VecProvider targetPos, VecProvider sourcePos, ServerEntity sourceEntity) {
        Provider.Context context = Provider.Context.of(sourceEntity, sourceEntity.getGame().getGametime());
        Vector2 pos = source != null ? source.get(context) : sourcePos.get(context);
        Vector2 direction = target != null ? target.get(context).sub(pos).nor() : targetPos.get(context).sub(pos).nor();
        float baseAngle = -arcGap * (bulletCount - 1) / 2f;

        for (int i = 0; i < bulletCount; ++i) {
            float angle = baseAngle + i * arcGap;

            Map<String, Object> customData = new HashMap<>();
            customData.put("direction", direction.cpy().rotateDeg(angle + angleOffset.get(context)));
            if (sourceEntity instanceof ServerLivingEntity livingEntity)
                customData.put("damage", Math.round(damage.getInt(context) * .5f + livingEntity.getStats().attack.value() / 50f));

            BulletEntityType bulletType = sourceEntity.getGame().getDataManager().getBullet(bulletId);
            if (bulletType != null) {
                ServerBulletEntity bullet = sourceEntity.getGame().getEntityManager().addEntity(UUID.randomUUID(), bulletType, pos.cpy(), customData);
                bullet.setParent(sourceEntity);
                bullet.setIndex(i % 2 == 0 ? 1 : -1);
            }
        }
    }
}
