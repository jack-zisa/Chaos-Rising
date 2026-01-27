package dev.creoii.chaos.world.setpiece;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.World;
import dev.creoii.chaos.util.Identifiable;

public interface Setpiece extends Identifiable {
    Codec<Setpiece> DISPATCH_CODEC = Type.CODEC.dispatch(
        Setpiece::getType,
        type -> switch (type) {
            case LAYERED_AREA -> LayeredAreaSetpiece.CODEC;
            case RANDOM_WALK -> RandomWalkSetpiece.CODEC;
        }
    );

    Type getType();

    void place(World world, int x, int y);

    enum Type {
        LAYERED_AREA,
        RANDOM_WALK;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
