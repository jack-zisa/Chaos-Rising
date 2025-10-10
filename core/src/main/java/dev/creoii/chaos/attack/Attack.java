package dev.creoii.chaos.attack;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.util.provider.vecprovider.SourcePosVecProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public interface Attack {
    Codec<Attack> CODEC = Type.CODEC.dispatch(Attack::getType, type -> switch (type) {
        case SIMPLE -> SimpleAttack.CODEC;
        case MULTI -> MultiAttack.CODEC;
    });

    Type getType();

    void attack(VecProvider targetPos, VecProvider sourcePos, Entity sourceEntity);

    default void attack(VecProvider targetPos, Entity sourceEntity) {
        attack(targetPos, new SourcePosVecProvider(), sourceEntity);
    }

    enum Type {
        SIMPLE,
        MULTI;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
