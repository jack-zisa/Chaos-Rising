package dev.creoii.chaos.util.provider.entityprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.Entity;

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
    public Entity get(Context context) {
        return entities.get(context.random().nextInt(entities.size())).get(context);
    }
}
