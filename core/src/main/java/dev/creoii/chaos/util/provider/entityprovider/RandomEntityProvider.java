package dev.creoii.chaos.util.provider.entityprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;

import javax.annotation.Nullable;
import java.util.List;

public record RandomEntityProvider(List<EntityProvider> entities) implements EntityProvider {
    public static final MapCodec<RandomEntityProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        EntityProvider.CODEC.listOf().fieldOf("entities").forGetter(RandomEntityProvider::entities)
    ).apply(instance, RandomEntityProvider::new));

    @Override
    public Type getType() {
        return Type.RANDOM;
    }

    @Override
    @Nullable
    public Entity get(ContextProvider context) {
        if (context.has(ComponentTypes.RANDOM)) {
            return entities.get(context.get(ComponentTypes.RANDOM).nextInt(entities.size())).get(context);
        }
        return null;
    }
}
