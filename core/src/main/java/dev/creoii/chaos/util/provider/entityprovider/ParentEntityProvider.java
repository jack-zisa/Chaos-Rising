package dev.creoii.chaos.util.provider.entityprovider;

import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;

import javax.annotation.Nullable;

public record ParentEntityProvider() implements EntityProvider {
    public static final ParentEntityProvider INSTANCE = new ParentEntityProvider();
    public static final MapCodec<ParentEntityProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.PARENT;
    }

    @Override
    @Nullable
    public Entity get(ContextProvider context) {
        if (context.has(ComponentTypes.ENTITY, ComponentTypes.WORLD)) {
            if (context.get(ComponentTypes.ENTITY) instanceof LivingEntity living && living.hasParent()) {
                return (Entity) context.get(ComponentTypes.WORLD).getEntityManager().getEntity(living.getParentId());
            }
        }
        return null;
    }
}
