package dev.creoii.chaos.world.dungeon;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.World;
import dev.creoii.chaos.util.Direction;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.world.dungeon.room.Connection;
import dev.creoii.chaos.world.dungeon.room.Room;
import dev.creoii.chaos.world.dungeon.room.RoomGenerator;

import java.util.*;

public class DungeonGenerator {
    private final Dungeon dungeon;
    private final int x, y;
    private final Map<String, Integer> roomCounts;
    private final Set<PlacedRoom> rooms;
    private int depth;
    private int maxDepth;

    public DungeonGenerator(Dungeon dungeon, int x, int y) {
        this.dungeon = dungeon;
        this.x = x;
        this.y = y;
        roomCounts = new HashMap<>();
        rooms = new HashSet<>();
        depth = 0;
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

    public void generate(World world) {
        maxDepth = dungeon.maxDepth().getInt(new Provider.Context(world.getGame(), world, null, world.getGame().getGametime(), new Vector2(x, y), world.getRandom()));
        beginGenerate(world);
    }

    private void beginGenerate(World world) {
        RoomGenerator generator = new RoomGenerator(dungeon.fallback().get(new Provider.Context(world.getGame(), world, null, world.getGame().getGametime(), new Vector2(x, y), world.getRandom())), x, y, null);

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

        RoomGenerator generator = new RoomGenerator(dungeon.fallback().get(new Provider.Context(world.getGame(), world, null, world.getGame().getGametime(), new Vector2(parentConnection.x(), parentConnection.y()), world.getRandom())), parentConnection.x(), parentConnection.y(), parentConnection.direction());

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

    public record PendingRoom(int x, int y, int width, int height, Direction direction) implements Room { }

    public record PlacedRoom(int x, int y, int width, int height, Direction direction) implements Room {
        public PlacedRoom(PendingRoom pendingRoom) {
            this(pendingRoom.x(), pendingRoom.y(), pendingRoom.width(), pendingRoom.height(), pendingRoom.direction());
        }
    }
}
