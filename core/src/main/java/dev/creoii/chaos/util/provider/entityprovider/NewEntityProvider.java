package dev.creoii.chaos.util.provider.entityprovider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.World;
import dev.creoii.chaos.entity.EnemyEntityType;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;

import java.util.HashMap;

public record NewEntityProvider(String id) implements EntityProvider {
    public static final MapCodec<NewEntityProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.STRING.fieldOf("id").forGetter(NewEntityProvider::id)
    ).apply(instance, NewEntityProvider::new));

    @Override
    public Type getType() {
        return Type.NEW;
    }

    @Override
    public Entity get(ContextProvider context) {
        if (context.has(ComponentTypes.WORLD, ComponentTypes.POS)) {
            EnemyEntityType entityType = DataManager.getEnemy(id);
            if (entityType != null) {
                World world = context.get(ComponentTypes.WORLD);
                return entityType.create(world, world.getEntityManager().getNextId(), context.get(ComponentTypes.POS).cpy(), new HashMap<>());
            }
        }
        return null;
    }
}
