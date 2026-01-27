package dev.creoii.chaos.world.map;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.World;
import dev.creoii.chaos.util.Identifiable;

public interface WorldMap extends Identifiable {
    Codec<WorldMap> DISPATCH_CODEC = Type.CODEC.dispatch(
        WorldMap::getType,
        type -> switch (type) {
            case LAYERED -> LayeredWorldMap.CODEC;
            case NOISE_BASED -> NoiseBasedWorldMap.CODEC;
        }
    );

    Type getType();

    int getWidth();

    int getHeight();

    void place(World world);

    enum Type {
        LAYERED,
        NOISE_BASED;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
