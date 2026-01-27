package dev.creoii.chaos.util.provider.tileprovider;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.world.tile.Tile;

import java.util.function.Function;

public interface TileProvider extends Provider<Tile> {
    Codec<TileProvider> TYPE_CODEC = Type.CODEC.dispatch(TileProvider::getType, type -> switch (type) {
        case SIMPLE -> SimpleTileProvider.CODEC;
        case RANDOM -> RandomTileProvider.CODEC;
    });
    Codec<TileProvider> CODEC = Codec.either(Tile.ID_CODEC, TYPE_CODEC).xmap(either -> {
        return either.map(SimpleTileProvider::new, Function.identity());
    }, Either::right);

    Type getType();

    enum Type {
        SIMPLE,
        RANDOM;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
