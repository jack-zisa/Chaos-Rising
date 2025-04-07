package dev.creoii.chaos.util;

import com.badlogic.gdx.math.Vector2;

public interface Positionable {
    Vector2 getPos();

    void setPos(Vector2 pos);

    default void setPos(float x, float y) {
        setPos(new Vector2(x, y));
    }
}
