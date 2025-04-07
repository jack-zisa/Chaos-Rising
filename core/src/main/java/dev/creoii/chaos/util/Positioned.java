package dev.creoii.chaos.util;

import com.badlogic.gdx.math.Vector2;

public interface Positioned {
    Vector2 getPos();

    void setPos(Vector2 pos);

    default void setPos(int x, int y) {
        setPos(new Vector2(x, y));
    }
}
