package dev.creoii.chaos.render.screen.widget;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.util.Inputtable;
import dev.creoii.chaos.util.Renderable;

public abstract class Widget implements Renderable, Inputtable {
    private final Vector2 pos;

    protected Widget(Vector2 pos) {
        this.pos = pos;
    }

    public Vector2 getPos() {
        return pos;
    }
}
