package dev.creoii.chaos.world.dungeon.room;

import dev.creoii.chaos.util.Direction;

public interface Room {
    int x();

    int y();

    int width();

    int height();

    Direction direction();

    static boolean intersects(Room a, Room b) {
        return a.x() < b.x() + a.width() && a.x() + a.width() > b.x() && a.y() < b.y() + b.height() && a.y() + a.height() > b.y();
    }
}
