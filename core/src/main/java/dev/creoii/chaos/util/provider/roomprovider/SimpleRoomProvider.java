package dev.creoii.chaos.util.provider.roomprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.world.dungeon.room.RoomTemplate;

public record SimpleRoomProvider(RoomTemplate room) implements RoomProvider {
    public static final MapCodec<SimpleRoomProvider> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            RoomTemplate.ID_CODEC.fieldOf("room").forGetter(SimpleRoomProvider::room)
        ).apply(instance, SimpleRoomProvider::new)
    );

    @Override
    public Type getType() {
        return Type.SIMPLE;
    }

    @Override
    public RoomTemplate get(ContextProvider context) {
        return room;
    }
}
