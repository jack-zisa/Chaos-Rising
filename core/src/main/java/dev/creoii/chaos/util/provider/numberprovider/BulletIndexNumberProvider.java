package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;

public record BulletIndexNumberProvider() implements NumberProvider {
    private static final BulletIndexNumberProvider INSTANCE = new BulletIndexNumberProvider();
    public static final MapCodec<BulletIndexNumberProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.BULLET_INDEX;
    }

    @Override
    public Float get(ContextProvider context) {
        if (context.get(ComponentTypes.ENTITY) instanceof BulletEntity bullet) {
            return (float) bullet.getIndex();
        }
        return 1f;
    }

    @Override
    public BulletIndexNumberProvider copy() {
        return new BulletIndexNumberProvider();
    }

    public BulletIndexNumberProvider init(int startTime) {
        return this;
    }
}
