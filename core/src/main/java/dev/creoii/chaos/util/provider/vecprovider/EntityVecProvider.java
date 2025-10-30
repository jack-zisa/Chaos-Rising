package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.util.provider.entityprovider.EntityProvider;

public record EntityVecProvider(EntityProvider entity) implements VecProvider {
    public static final MapCodec<EntityVecProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        EntityProvider.CODEC.fieldOf("entity").forGetter(EntityVecProvider::entity)
    ).apply(instance, EntityVecProvider::new));

    @Override
    public Type getType() {
        return Type.ENTITY;
    }

    @Override
    public Vector2 get(Context context) {
        Entity entity = this.entity.get(context);
        if (entity == null)
            return Vector2.Zero;
        return entity.getPos().cpy();
    }

    @Override
    public VecProvider copy() {
        return new EntityVecProvider(entity);
    }
}
