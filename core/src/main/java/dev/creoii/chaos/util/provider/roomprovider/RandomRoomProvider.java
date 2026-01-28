package dev.creoii.chaos.util.provider.roomprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.world.dungeon.room.RoomTemplate;

import java.util.List;

public record RandomRoomProvider(List<RoomProvider> values) implements RoomProvider {
    public static final MapCodec<RandomRoomProvider> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            RoomProvider.CODEC.listOf().fieldOf("values").forGetter(RandomRoomProvider::values)
        ).apply(instance, RandomRoomProvider::new)
    );

    @Override
    public Type getType() {
        return Type.RANDOM;
    }

    @Override
    public RoomTemplate get(Context context) {
        return values.get(context.random().nextInt(values.size())).get(context);
    }
}
