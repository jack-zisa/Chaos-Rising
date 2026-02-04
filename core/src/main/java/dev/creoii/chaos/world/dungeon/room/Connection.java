package dev.creoii.chaos.world.dungeon.room;

import dev.creoii.chaos.util.Direction;

public class Connection {
    private final int localX;
    private final int localY;
    private int x;
    private int y;
    private final Direction direction;
    private Connection connection = null;

    public Connection(int localX, int localY, int x, int y, Direction direction) {
        this.localX = localX;
        this.localY = localY;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int localX() {
        return localX;
    }

    public int localY() {
        return localY;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Direction direction() {
        return direction;
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void setConnected(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnected() {
        return connection;
    }
}
