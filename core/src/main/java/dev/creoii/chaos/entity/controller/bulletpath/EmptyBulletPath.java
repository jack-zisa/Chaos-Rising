package dev.creoii.chaos.entity.controller.bulletpath;

import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.controller.EntityController;

public record EmptyBulletPath() implements BulletPath {
    public static final EmptyBulletPath INSTANCE = new EmptyBulletPath();
    public static final MapCodec<EmptyBulletPath> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.EMPTY;
    }

    @Override
    public float speed(EntityController<? extends BulletEntity> controller) {
        return 0f;
    }

    @Override
    public void update(EntityController<? extends BulletEntity> controller, int gametime, float dt) {
    }

    @Override
    public BulletPath copy() {
        return this;
    }
}
