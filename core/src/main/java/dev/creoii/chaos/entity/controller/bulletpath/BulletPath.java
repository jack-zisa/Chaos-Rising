package dev.creoii.chaos.entity.controller.bulletpath;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.controller.EntityController;

public interface BulletPath {
    Codec<BulletPath> CODEC = BulletPath.Type.CODEC.dispatch(BulletPath::getType, type -> switch (type) {
        case EMPTY -> EmptyBulletPath.CODEC;
        case SEGMENTED -> SegmentedBulletPath.CODEC;
        case SIMPLE -> SimpleBulletPath.CODEC;
    });

    Type getType();

    float speed(EntityController<? extends BulletEntity> controller);

    void update(EntityController<? extends BulletEntity> controller, int gametime, float dt);

    BulletPath copy();

    enum Type {
        EMPTY,
        SEGMENTED,
        SIMPLE;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
