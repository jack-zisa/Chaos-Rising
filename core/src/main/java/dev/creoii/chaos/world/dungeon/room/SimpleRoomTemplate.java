package dev.creoii.chaos.world.dungeon.room;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.World;
import dev.creoii.chaos.util.Direction;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.Context;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;
import dev.creoii.chaos.util.provider.tileprovider.TileProvider;
import dev.creoii.chaos.world.dungeon.DungeonGenerator;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record SimpleRoomTemplate(String id, NumberProvider width, NumberProvider height, TileProvider floorProvider, TileProvider wallProvider, TileProvider hallwayProvider, NumberProvider connections) implements RoomTemplate {
    public static final MapCodec<SimpleRoomTemplate> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("id").forGetter(SimpleRoomTemplate::id),
            NumberProvider.CODEC.fieldOf("width").forGetter(SimpleRoomTemplate::width),
            NumberProvider.CODEC.fieldOf("height").forGetter(SimpleRoomTemplate::height),
            TileProvider.CODEC.fieldOf("floor_provider").forGetter(SimpleRoomTemplate::floorProvider),
            TileProvider.CODEC.fieldOf("wall_provider").forGetter(SimpleRoomTemplate::wallProvider),
            TileProvider.CODEC.fieldOf("hallway_provider").forGetter(SimpleRoomTemplate::hallwayProvider),
            NumberProvider.CODEC.fieldOf("max_connections").forGetter(SimpleRoomTemplate::connections)
        ).apply(instance, SimpleRoomTemplate::new);
    });

    @Override
    public Type getType() {
        return Type.SIMPLE;
    }

    @Override
    public DungeonGenerator.PendingRoom build(World world, DungeonGenerator dungeon, RoomGenerator room, @Nullable RoomGenerator parent) {
        int x = room.x();
        int y = room.y();
        Direction direction = room.direction();

        // sample width & height @ start pos
        Context context = Context.rootOf(world).with(ComponentTypes.POS, new Vector2(x, y));

        int width = this.width.getInt(context);
        int height = this.height.getInt(context);
        int maxConnections = this.connections.getInt(context);

        room.setWidth(width);
        room.setHeight(height);

        if (direction != null) {
            // offset for direction
            switch (direction) {
                case NORTH -> y += 1;
                case SOUTH -> y -= height;
                case EAST -> x += 1;
                case WEST -> x -= width;
            }

            if (parent != null) {
                // guaranteed connection point to previous room
                Connection connection = createEdgeConnection(x, y, width, height, direction.getOpposite(), world);
                room.getConnections().add(connection);
                --maxConnections;
            }
        }

        for (int i = 0; i < maxConnections; ++i) {
            Direction randomDirection = Direction.random(world.getRandom());
            Connection connection = createEdgeConnection(x, y, width, height, randomDirection, world);
            room.getConnections().add(connection);
        }

        if (parent != null && direction != null) {
            List<Connection> validParentConnections = parent.getConnections().stream().filter(connection -> connection.direction() == direction).collect(Collectors.toList());
            List<Connection> validChildConnections = room.getConnections().stream().filter(connection -> connection.direction() == direction.getOpposite()).collect(Collectors.toList());

            Collections.shuffle(validParentConnections);
            Collections.shuffle(validChildConnections);

            Connection parentConnection = validParentConnections.getFirst();
            Connection childConnection = validChildConnections.getFirst();

            if (parentConnection == null || childConnection == null)
                return null;
            childConnection.setConnected(parentConnection);

            // match positions
            x = parentConnection.x() - childConnection.localX();
            y = parentConnection.y() - childConnection.localY();

            switch (direction) {
                case NORTH -> y += 1;
                case SOUTH -> y -= 1;
                case EAST -> x += 1;
                case WEST -> x -= 1;
            }
        }

        for (Connection c : room.getConnections()) {
            c.setPos(x + c.localX(), y + c.localY());
        }

        return new DungeonGenerator.PendingRoom(x, y, width, height, direction);
    }

    @Override
    public DungeonGenerator.PlacedRoom place(World world, DungeonGenerator dungeon, RoomGenerator room, DungeonGenerator.PendingRoom pendingRoom, int depth) {
        Context context = Context.rootOf(world);
        for (int rx = 0; rx < pendingRoom.width(); ++rx) {
            for (int ry = 0; ry < pendingRoom.height(); ++ry) {
                Context child = context.child().with(ComponentTypes.POS, new Vector2(pendingRoom.x() + rx, pendingRoom.y() + ry));
                if (rx == 0 || ry == 0 || rx == pendingRoom.width() - 1 || ry == pendingRoom.height() - 1) {
                    world.setGround(pendingRoom.x() + rx, pendingRoom.y() + ry, wallProvider.get(child));
                } else {
                    world.setGround(pendingRoom.x() + rx, pendingRoom.y() + ry, floorProvider.get(child));
                }
            }
        }

        room.getConnections().forEach(connection -> {
            if (connection.isConnected()) {
                Context child = context.child().with(ComponentTypes.POS, new Vector2(connection.x(), connection.y()));
                Context backChild = context.child().with(ComponentTypes.POS, new Vector2(connection.x() + connection.direction().getUnitX(), connection.y() + connection.direction().getUnitY()));
                world.setGround(connection.x(), connection.y(), hallwayProvider.get(child));
                world.setGround(connection.x() + connection.direction().getUnitX(), connection.y() + connection.direction().getUnitY(), hallwayProvider.get(backChild));
            }
        });

        return new DungeonGenerator.PlacedRoom(pendingRoom, depth);
    }

    private static Connection createEdgeConnection(int x, int y, int width, int height, Direction direction, World world) {
        return switch (direction) {
            case NORTH -> new Connection(world.getRandom().nextInt(width - 2) + 1, height - 1, x + (world.getRandom().nextInt(width - 2) + 1), y + (height - 1), Direction.NORTH);
            case SOUTH -> new Connection(world.getRandom().nextInt(width - 2) + 1, 0, x + (world.getRandom().nextInt(width - 2) + 1), y, Direction.SOUTH);
            case WEST -> new Connection(0, world.getRandom().nextInt(height - 2) + 1, x, y + (world.getRandom().nextInt(height - 2) + 1), Direction.WEST);
            case EAST -> new Connection(width - 1, world.getRandom().nextInt(height - 2) + 1, x + (width - 1), y + (world.getRandom().nextInt(height - 2) + 1), Direction.EAST);
        };
    }
}
