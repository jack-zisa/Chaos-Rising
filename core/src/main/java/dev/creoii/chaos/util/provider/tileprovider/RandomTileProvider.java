package dev.creoii.chaos.util.provider.tileprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.world.tile.Tile;

import javax.annotation.Nullable;
import java.util.List;

public record RandomTileProvider(List<TileProvider> values) implements TileProvider {
    public static final MapCodec<RandomTileProvider> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            TileProvider.CODEC.listOf().fieldOf("values").forGetter(RandomTileProvider::values)
        ).apply(instance, RandomTileProvider::new)
    );

    @Override
    public Type getType() {
        return Type.RANDOM;
    }

    @Override
    @Nullable
    public Tile get(ContextProvider context) {
        return context.has(ComponentTypes.RANDOM) ? values.get(context.get(ComponentTypes.RANDOM).nextInt(values.size())).get(context) : null;
    }
}
