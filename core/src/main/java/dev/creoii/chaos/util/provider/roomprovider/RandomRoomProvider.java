package dev.creoii.chaos.util.provider.roomprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.world.dungeon.room.RoomTemplate;

import javax.annotation.Nullable;
import java.util.List;

public record RandomRoomProvider(List<RoomProvider> rooms) implements RoomProvider {
    public static final MapCodec<RandomRoomProvider> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            RoomProvider.CODEC.listOf().fieldOf("rooms").forGetter(RandomRoomProvider::rooms)
        ).apply(instance, RandomRoomProvider::new)
    );

    @Override
    public Type getType() {
        return Type.RANDOM;
    }

    @Override
    @Nullable
    public RoomTemplate get(ContextProvider context) {
        return context.has(ComponentTypes.RANDOM) ? rooms.get(context.get(ComponentTypes.RANDOM).nextInt(rooms.size())).get(context) : null;
    }
}
