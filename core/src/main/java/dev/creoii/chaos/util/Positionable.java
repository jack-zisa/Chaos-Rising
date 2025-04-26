package dev.creoii.chaos.util;

import com.badlogic.gdx.math.Vector2;

public interface Positionable {
    Vector2 getPos();

    default void setPos(float x, float y) {
        getPos().x = x;
        getPos().y = y;
    }
}
