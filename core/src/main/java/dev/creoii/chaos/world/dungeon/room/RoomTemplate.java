package dev.creoii.chaos.world.dungeon.room;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.World;
import dev.creoii.chaos.util.Identifiable;
import dev.creoii.chaos.world.dungeon.DungeonGenerator;

import javax.annotation.Nullable;

public interface RoomTemplate extends Identifiable {
    Codec<RoomTemplate> DISPATCH_CODEC = Type.CODEC.dispatch(
        RoomTemplate::getType,
        type -> switch (type) {
            case SIMPLE -> SimpleRoomTemplate.CODEC;
        }
    );
    Codec<RoomTemplate> ID_CODEC = Codec.STRING.xmap(DataManager::getRoomTemplate, RoomTemplate::id);

    Type getType();

    @Nullable
    default DungeonGenerator.PendingRoom build(World world, DungeonGenerator dungeon, RoomGenerator room) {
        return build(world, dungeon, room, null);
    }

    /**
     * Setup exact placement information of the room, including position, size & direction
     */
    @Nullable
    DungeonGenerator.PendingRoom build(World world, DungeonGenerator dungeon, RoomGenerator room, @Nullable RoomGenerator parent);

    /**
     * Place tiles based on placement information created in {@link RoomTemplate#build(World, DungeonGenerator, RoomGenerator, RoomGenerator)}
     */
    @Nullable
    DungeonGenerator.PlacedRoom place(World world, DungeonGenerator dungeon, RoomGenerator room, DungeonGenerator.PendingRoom pendingRoom, int depth);

    enum Type {
        SIMPLE;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
