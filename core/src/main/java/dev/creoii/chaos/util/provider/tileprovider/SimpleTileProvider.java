package dev.creoii.chaos.util.provider.tileprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.world.tile.Tile;

public record SimpleTileProvider(Tile value) implements TileProvider {
    public static final SimpleTileProvider EMPTY = new SimpleTileProvider(DataManager.getTile("air"));
    public static final MapCodec<SimpleTileProvider> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Tile.ID_CODEC.fieldOf("value").forGetter(SimpleTileProvider::value)
        ).apply(instance, SimpleTileProvider::new)
    );

    @Override
    public Type getType() {
        return Type.SIMPLE;
    }

    @Override
    public Tile get(ContextProvider context) {
        return value;
    }
}
