package dev.creoii.chaos.world.dungeon.room;

import dev.creoii.chaos.World;
import dev.creoii.chaos.util.Direction;
import dev.creoii.chaos.world.dungeon.DungeonGenerator;

import java.util.ArrayList;
import java.util.List;

public class RoomGenerator implements Room {
    private final RoomTemplate roomTemplate;
    private final Direction direction;
    private final List<Connection> connections;
    private final int x;
    private final int y;
    private int width = -1, height = -1;

    public RoomGenerator(RoomTemplate roomTemplate, int x, int y, Direction direction) {
        this.roomTemplate = roomTemplate;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.connections = new ArrayList<>();
    }

    public RoomTemplate getRoom() {
        return roomTemplate;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Direction direction() {
        return direction;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public DungeonGenerator.PendingRoom build(World world, DungeonGenerator dungeonGenerator) {
        return roomTemplate.build(world, dungeonGenerator, this);
    }

    public DungeonGenerator.PendingRoom build(World world, DungeonGenerator dungeonGenerator, RoomGenerator parent) {
        return roomTemplate.build(world, dungeonGenerator, this, parent);
    }

    public DungeonGenerator.PlacedRoom place(World world, DungeonGenerator dungeonGenerator, DungeonGenerator.PendingRoom pendingRoom) {
        String roomId = roomTemplate.id();

        // Increment room count
        if (dungeonGenerator.getRoomCounts().containsKey(roomId)) {
            dungeonGenerator.getRoomCounts().put(roomId, dungeonGenerator.getRoomCounts().get(roomId) + 1);
        } else dungeonGenerator.getRoomCounts().put(roomId, 1);

        return roomTemplate.place(world, dungeonGenerator, this, pendingRoom);
    }
}
