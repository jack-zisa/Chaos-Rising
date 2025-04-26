package dev.creoii.chaos.util.provider.numberprovider;

import dev.creoii.chaos.entity.ServerBulletEntity;

public class BulletIndexNumberProvider implements NumberProvider {
    @Override
    public Float get(Context context) {
        return context.sourceEntity() instanceof ServerBulletEntity bullet ? (float) bullet.getIndex() : 1f;
    }

    @Override
    public BulletIndexNumberProvider copy() {
        return new BulletIndexNumberProvider();
    }

    public BulletIndexNumberProvider init(int startTime) {
        return this;
    }
}
