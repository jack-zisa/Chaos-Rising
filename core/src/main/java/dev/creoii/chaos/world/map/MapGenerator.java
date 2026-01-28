package dev.creoii.chaos.world.map;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.World;
import dev.creoii.chaos.util.Identifiable;

public interface MapGenerator extends Identifiable {
    Codec<MapGenerator> DISPATCH_CODEC = Type.CODEC.dispatch(
        MapGenerator::getType,
        type -> switch (type) {
            case LAYERED -> LayeredMapGenerator.CODEC;
            case NOISE_BASED -> NoiseBasedMapGenerator.CODEC;
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
