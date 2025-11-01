package dev.creoii.chaos.attack;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.entity.*;
import dev.creoii.chaos.item.EquipmentItem;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.numberprovider.ConstantNumberProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;
import dev.creoii.chaos.util.provider.vecprovider.ConstantVecProvider;
import dev.creoii.chaos.util.provider.vecprovider.SourceVecProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

import javax.annotation.Nullable;
import java.util.*;

public record SimpleAttack(String bulletId, NumberProvider damage, int bulletCount, int arcGap, float predictability, NumberProvider angleOffset, Optional<VecProvider> source, Optional<VecProvider> target) implements Attack {
    public static final MapCodec<SimpleAttack> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("bullet_id").forGetter(SimpleAttack::bulletId),
            NumberProvider.CODEC.fieldOf("damage").forGetter(SimpleAttack::damage),
            Codec.INT.fieldOf("bullet_count").orElse(1).forGetter(SimpleAttack::bulletCount),
            Codec.INT.fieldOf("arc_gap").orElse(0).forGetter(SimpleAttack::arcGap),
            Codec.FLOAT.fieldOf("predictability").orElse(0f).forGetter(SimpleAttack::predictability),
            NumberProvider.CODEC.fieldOf("angle_offset").orElse(ConstantNumberProvider.ZERO).forGetter(SimpleAttack::angleOffset),
            VecProvider.CODEC.optionalFieldOf("source").orElse(Optional.of(new SourceVecProvider())).forGetter(SimpleAttack::source),
            VecProvider.CODEC.optionalFieldOf("target").orElse(Optional.of(ConstantVecProvider.ZERO)).forGetter(SimpleAttack::target)
        ).apply(instance, SimpleAttack::new);
    });

    @Override
    public Type getType() {
        return Type.SIMPLE;
    }

    public void attack(VecProvider targetPos, VecProvider sourcePos, Entity sourceEntity, @Nullable EquipmentItem item) {
        if (!Attacker.canAttack(sourceEntity, item)) {
            return;
        }

        Provider.Context context = Provider.Context.of(sourceEntity, sourceEntity.getGame().getGametime());
        Vector2 pos = source.isPresent() ? source.get().get(context) : sourcePos.get(context);
        Vector2 direction = target.isPresent() ? target.get().get(context).sub(pos).nor() : targetPos.get(context).sub(pos).nor();
        float baseAngle = -arcGap * (bulletCount - 1) / 2f;

        List<BulletEntity> bullets = new ArrayList<>();
        for (int i = 0; i < bulletCount; ++i) {
            float angle = baseAngle + i * arcGap;

            BulletEntityType bulletType = DataManager.getBullet(bulletId);
            if (bulletType != null) {
                BulletEntity bullet = bulletType.create(context.game(), context.game().getEntityManager().getNextId(), pos.cpy(), new HashMap<>());
                bullet.setDamage(sourceEntity instanceof LivingEntity living ? Math.round(damage.getInt(context) * .5f + living.getStats().attack().value() / 50f) : 0);
                bullet.setIndex(i % 2 == 0 ? 1 : -1);
                bullet.setDirection(direction.cpy().rotateDeg(angle + angleOffset.get(context)));
                bullet.setParent(sourceEntity);
                bullets.add(bullet);
            }
        }

        sourceEntity.getGame().getEntityManager().addEntities(bullets);

        ((Attacker) sourceEntity).setLastAttackTime(System.currentTimeMillis());
    }
}
