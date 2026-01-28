package dev.creoii.chaos.util.provider.roomprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.world.dungeon.room.RoomTemplate;

public record SimpleRoomProvider(RoomTemplate value) implements RoomProvider {
    public static final MapCodec<SimpleRoomProvider> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            RoomTemplate.ID_CODEC.fieldOf("value").forGetter(SimpleRoomProvider::value)
        ).apply(instance, SimpleRoomProvider::new)
    );

    @Override
    public Type getType() {
        return Type.SIMPLE;
    }

    @Override
    public RoomTemplate get(Context context) {
        return value;
    }
}
