package dev.creoii.chaos.util.provider.booleanprovider;

import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;

public record HasParentBooleanProvider() implements BooleanProvider {
    private static final HasParentBooleanProvider INSTANCE = new HasParentBooleanProvider();
    public static final MapCodec<HasParentBooleanProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.HAS_PARENT;
    }

    @Override
    public Boolean get(ContextProvider context) {
        if (context.get(ComponentTypes.ENTITY) instanceof LivingEntity livingEntity) {
            return livingEntity.hasParent();
        }
        return false;
    }

    @Override
    public HasParentBooleanProvider copy() {
        return INSTANCE;
    }

    public HasParentBooleanProvider init(int startTime) {
        return this;
    }
}
