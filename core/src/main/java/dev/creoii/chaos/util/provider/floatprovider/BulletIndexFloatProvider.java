package dev.creoii.chaos.util.provider.floatprovider;

import dev.creoii.chaos.entity.BulletEntity;

public class BulletIndexFloatProvider implements FloatProvider {
    @Override
    public Float get(Context context) {
        return context.sourceEntity() instanceof BulletEntity bullet ? (float) bullet.getIndex() : 1f;
    }

    @Override
    public BulletIndexFloatProvider copy() {
        return new BulletIndexFloatProvider();
    }

    public BulletIndexFloatProvider init(int startTime) {
        return this;
    }
}
