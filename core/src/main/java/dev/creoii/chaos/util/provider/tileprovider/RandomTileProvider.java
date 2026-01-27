package dev.creoii.chaos.util.provider.tileprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.world.tile.Tile;

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
    public Tile get(Context context) {
        return values.get(context.random().nextInt(values.size())).get(context);
    }
}
