package dev.creoii.chaos.util;

import com.badlogic.gdx.math.Vector2;

import java.util.Map;
import java.util.Random;

public enum Direction {
    NORTH(new Vector2(0f, 1f), Axis.Y),
    SOUTH(new Vector2(0f, -1f), Axis.Y),
    EAST(new Vector2(0f, 0f), Axis.X),
    WEST(new Vector2(-1f, 0f), Axis.X);

    private static final Map<Direction, Direction> OPPOSITES = Map.of(NORTH, SOUTH, SOUTH, NORTH, EAST, WEST, WEST, EAST);
    private final Vector2 unit;
    private final Axis axis;

    Direction(Vector2 unit, Axis axis) {
        this.unit = unit;
        this.axis = axis;
    }

    public static Direction random(Random random) {
        return Direction.values()[random.nextInt(Direction.values().length)];
    }

    public static Direction random(Random random, Axis axis) {
        return axis == Axis.X ? random.nextBoolean() ? EAST : WEST : random.nextBoolean() ? NORTH : SOUTH;
    }

    public Vector2 getUnit() {
        return unit;
    }

    public Axis getAxis() {
        return axis;
    }

    public Direction getOpposite() {
        return OPPOSITES.get(this);
    }

    public enum Axis {
        X,
        Y
    }
}
