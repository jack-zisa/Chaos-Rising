package dev.creoii.chaos.world.dungeon;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.World;
import dev.creoii.chaos.util.Direction;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.Context;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.world.dungeon.room.Connection;
import dev.creoii.chaos.world.dungeon.room.Room;
import dev.creoii.chaos.world.dungeon.room.RoomGenerator;
import dev.creoii.chaos.world.dungeon.room.RoomTemplate;
import it.unimi.dsi.fastutil.Pair;

import java.util.*;

public class DungeonGenerator implements ContextProvider {
    private final World world;
    private final Dungeon dungeon;
    private final int x, y;
    private final Map<String, Integer> roomCounts;
    private final Set<Pair<RoomGenerator, PendingRoom>> pendingRooms;
    private int maxDepth;
    private final Context context;
    private final Context roomContext;
    private PlacedRoom startRoom;

    public DungeonGenerator(World world, Dungeon dungeon, int x, int y) {
        this.world = world;
        this.dungeon = dungeon;
        this.x = x;
        this.y = y;
        roomCounts = new HashMap<>();
        pendingRooms = new HashSet<>();
        context = Context.rootOf(world).with(ComponentTypes.POS, new Vector2(x, y)).with(ComponentTypes.DUNGEON, this);
        roomContext = context.child();
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Map<String, Integer> getRoomCounts() {
        return roomCounts;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public Set<Pair<RoomGenerator, PendingRoom>> getPendingRooms() {
        return pendingRooms;
    }

    public PlacedRoom getStartRoom() {
        return startRoom;
    }

    public void incrementRoomCount(String id) {
        if (roomCounts.containsKey(id)) {
            roomCounts.put(id, roomCounts.get(id) + 1);
        } else roomCounts.put(id, 1);
    }

    public void build() {
        maxDepth = dungeon.maxDepth().getInt(context.child().with(ComponentTypes.POS, new Vector2(x, y)));
        beginBuild();
    }

    private void beginBuild() {
        roomContext.clearLocal();
        roomContext.set(ComponentTypes.POS, new Vector2(x, y));
        roomContext.set(ComponentTypes.ROOM_DEPTH, 0);

        RoomTemplate template = dungeon.fallback().get(roomContext);
        RoomGenerator generator = new RoomGenerator(template, x, y, null, 0);

        roomContext.set(ComponentTypes.ROOM, generator);

        PendingRoom pendingRoom = generator.build(world, this);

        if (pendingRoom == null)
            return;

        pendingRooms.add(Pair.of(generator, pendingRoom));
        incrementRoomCount(template.id());

        for (Connection connection : generator.getConnections()) {
            generateRoom(world, generator, connection);
        }
    }

    public void generateRoom(World world, RoomGenerator parent, Connection parentConnection) {
        roomContext.clearLocal();
        roomContext.set(ComponentTypes.POS, new Vector2(x, y));
        roomContext.set(ComponentTypes.ROOM_DEPTH, parent.depth() + 1);

        RoomTemplate template = dungeon.fallback().get(roomContext);
        if (template == null)
            return;

        RoomGenerator generator = new RoomGenerator(template, parentConnection.x(), parentConnection.y(), parentConnection.direction(), parent.depth() + 1);

        roomContext.set(ComponentTypes.ROOM, generator);

        PendingRoom pendingRoom = generator.build(world, this, parent);

        if (pendingRoom == null)
            return;

        for (Pair<RoomGenerator, PendingRoom> other : pendingRooms) {
            if (Room.intersects(other.right(), pendingRoom)) {
                return;
            }
        }

        pendingRooms.add(Pair.of(generator, pendingRoom));
        incrementRoomCount(template.id());

        if (generator.depth() >= maxDepth)
            return;

        for (Connection childConnection : generator.getConnections()) {
            generateRoom(world, generator, childConnection);
        }
    }

    public void place() {
        for (Pair<RoomGenerator, PendingRoom> pair : pendingRooms) {
            PlacedRoom placedRoom = pair.left().place(world, this, pair.right());

            if (pair.left().depth() == 0) {
                startRoom = placedRoom;
            }
        }
    }

    @Override
    public Game getGame() {
        return world.getGame();
    }

    @Override
    public Context getContext() {
        return context;
    }

    public static class PendingRoom implements Room {
        private int x, y;
        private final int width, height;
        private final Direction direction;

        public PendingRoom(int x, int y, int width, int height, Direction direction) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.direction = direction;
        }

        @Override
        public int x() {
            return x;
        }

        @Override
        public int y() {
            return y;
        }

        public void setPos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int width() {
            return width;
        }

        @Override
        public int height() {
            return height;
        }

        @Override
        public Direction direction() {
            return direction;
        }
    }

    public record PlacedRoom(int x, int y, int width, int height, Direction direction, int depth) implements Room {
        public PlacedRoom(PendingRoom pendingRoom, int depth) {
            this(pendingRoom.x(), pendingRoom.y(), pendingRoom.width(), pendingRoom.height(), pendingRoom.direction(), depth);
        }
    }
}
