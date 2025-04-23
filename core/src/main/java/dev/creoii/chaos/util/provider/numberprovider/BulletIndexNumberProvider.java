package dev.creoii.chaos.util.provider.numberprovider;

import dev.creoii.chaos.entity.BulletEntity;

public class BulletIndexNumberProvider implements NumberProvider {
    @Override
    public Float get(Context context) {
        return context.sourceEntity() instanceof BulletEntity bullet ? (float) bullet.getIndex() : 1f;
    }

    @Override
    public BulletIndexNumberProvider copy() {
        return new BulletIndexNumberProvider();
    }

    public BulletIndexNumberProvider init(int startTime) {
        return this;
    }
}
