package dev.creoii.chaos.util.provider.roomprovider;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.world.dungeon.room.RoomTemplate;

import java.util.function.Function;

public interface RoomProvider extends Provider<RoomTemplate> {
    Codec<RoomProvider> TYPE_CODEC = Type.CODEC.dispatch(RoomProvider::getType, type -> switch (type) {
        case SIMPLE -> SimpleRoomProvider.CODEC;
        case RANDOM -> RandomRoomProvider.CODEC;
    });
    Codec<RoomProvider> CODEC = Codec.either(RoomTemplate.ID_CODEC, TYPE_CODEC).xmap(either -> {
        return either.map(SimpleRoomProvider::new, Function.identity());
    }, Either::right);

    Type getType();

    enum Type {
        SIMPLE,
        RANDOM;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
