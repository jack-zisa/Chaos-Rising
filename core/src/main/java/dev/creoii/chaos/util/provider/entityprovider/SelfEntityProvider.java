package dev.creoii.chaos.util.provider.entityprovider;

import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;

import javax.annotation.Nullable;

public record SelfEntityProvider() implements EntityProvider {
    public static final SelfEntityProvider INSTANCE = new SelfEntityProvider();
    public static final MapCodec<SelfEntityProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.SELF;
    }

    @Override
    @Nullable
    public Entity get(ContextProvider context) {
        return context.get(ComponentTypes.ENTITY, null);
    }
}
