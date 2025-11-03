package dev.creoii.chaos.util.provider.entityprovider;

import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.entity.Entity;

public record SelfEntityProvider() implements EntityProvider {
    public static final SelfEntityProvider INSTANCE = new SelfEntityProvider();
    public static final MapCodec<SelfEntityProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.SELF;
    }

    @Override
    public Entity get(Context context) {
        return context.entity();
    }
}
