package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.entity.BulletEntity;

public record BulletIndexNumberProvider() implements NumberProvider {
    private static final BulletIndexNumberProvider INSTANCE = new BulletIndexNumberProvider();
    public static final MapCodec<BulletIndexNumberProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.BULLET_INDEX;
    }

    @Override
    public Float get(Context context) {
        return context.entity() instanceof BulletEntity bullet ? (float) bullet.getIndex() : 1f;
    }

    @Override
    public BulletIndexNumberProvider copy() {
        return new BulletIndexNumberProvider();
    }

    public BulletIndexNumberProvider init(int startTime) {
        return this;
    }
}
