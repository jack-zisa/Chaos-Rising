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

import java.util.*;

public class DungeonGenerator implements ContextProvider {
    private final World world;
    private final Dungeon dungeon;
    private final int x, y;
    private final Map<String, Integer> roomCounts;
    private final Set<PlacedRoom> rooms;
    private int depth;
    private int maxDepth;
    private final Context context;
    private final Context roomContext;

    public DungeonGenerator(World world, Dungeon dungeon, int x, int y) {
        this.world = world;
        this.dungeon = dungeon;
        this.x = x;
        this.y = y;
        roomCounts = new HashMap<>();
        rooms = new HashSet<>();
        depth = 0;
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

    public Set<PlacedRoom> getRooms() {
        return rooms;
    }

    public int getDepth() {
        return depth;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void generate() {
        maxDepth = dungeon.maxDepth().getInt(context.child().with(ComponentTypes.POS, new Vector2(x, y)));
        beginGenerate();
    }

    private void beginGenerate() {
        roomContext.clearLocal();
        roomContext.set(ComponentTypes.POS, new Vector2(x, y));

        RoomGenerator generator = new RoomGenerator(dungeon.fallback().get(roomContext), x, y, null, depth + 1);

        roomContext.set(ComponentTypes.ROOM, generator);

        PendingRoom pendingRoom = generator.build(world, this);

        if (pendingRoom == null)
            return;

        for (PlacedRoom other : rooms) {
            if (Room.intersects(other, pendingRoom)) {
                return;
            }
        }

        PlacedRoom placedRoom = generator.place(world, this, pendingRoom);
        rooms.add(placedRoom);
        ++depth;

        for (Connection connection : generator.getConnections()) {
            generateRoom(world, generator, connection);
        }
    }

    public void generateRoom(World world, RoomGenerator parent, Connection parentConnection) {
        if (depth >= maxDepth) {
            return;
        }

        roomContext.clearLocal();
        roomContext.set(ComponentTypes.POS, new Vector2(x, y));

        RoomGenerator generator = new RoomGenerator(dungeon.fallback().get(roomContext), parentConnection.x(), parentConnection.y(), parentConnection.direction(), depth + 1);

        roomContext.set(ComponentTypes.ROOM, generator);

        PendingRoom pendingRoom = generator.build(world, this, parent);

        if (pendingRoom == null)
            return;

        for (PlacedRoom other : rooms) {
            if (Room.intersects(other, pendingRoom)) {
                return;
            }
        }

        PlacedRoom placedRoom = generator.place(world, this, pendingRoom);
        rooms.add(placedRoom);
        ++depth;

        for (Connection childConnection : generator.getConnections()) {
            generateRoom(world, generator, childConnection);
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

    public record PendingRoom(int x, int y, int width, int height, Direction direction) implements Room { }

    public record PlacedRoom(int x, int y, int width, int height, Direction direction, int depth) implements Room {
        public PlacedRoom(PendingRoom pendingRoom, int depth) {
            this(pendingRoom.x(), pendingRoom.y(), pendingRoom.width(), pendingRoom.height(), pendingRoom.direction(), depth);
        }
    }
}
