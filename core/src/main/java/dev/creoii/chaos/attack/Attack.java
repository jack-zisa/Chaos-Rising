package dev.creoii.chaos.attack;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.item.EquipmentItem;
import dev.creoii.chaos.util.provider.vecprovider.SourceVecProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

import javax.annotation.Nullable;

public interface Attack {
    Codec<Attack> CODEC = Type.CODEC.dispatch(Attack::getType, type -> switch (type) {
        case SIMPLE -> SimpleAttack.CODEC;
        case MULTI -> MultiAttack.CODEC;
    });

    Type getType();

    /**
     * @param item null if attacking from an {@link dev.creoii.chaos.entity.EnemyEntity}.
     */
    void attack(VecProvider targetPos, VecProvider sourcePos, Entity sourceEntity, EquipmentItem item, boolean force);

    default void attack(VecProvider targetPos, VecProvider sourcePos, Entity sourceEntity, EquipmentItem item) {
        attack(targetPos, sourcePos, sourceEntity, item, false);
    }

    default void attack(VecProvider targetPos, Entity sourceEntity, @Nullable EquipmentItem item) {
        attack(targetPos, SourceVecProvider.INSTANCE, sourceEntity, item, false);
    }

    enum Type {
        SIMPLE,
        MULTI;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
