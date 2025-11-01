package dev.creoii.chaos.util.provider.entityprovider;

import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LivingEntity;

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
    public Entity get(Context context) {
        if (context.sourceEntity() instanceof LivingEntity living && living.hasParent()) {
            return (Entity) context.game().getEntityManager().getEntity(living.getParentId());
        }
        return null;
    }
}
