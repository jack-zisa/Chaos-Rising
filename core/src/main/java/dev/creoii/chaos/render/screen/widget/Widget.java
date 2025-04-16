package dev.creoii.chaos.render.screen.widget;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.render.screen.Screen;
import dev.creoii.chaos.util.Inputtable;
import dev.creoii.chaos.util.Renderable;

public abstract class Widget implements Renderable, Inputtable {
    private final Screen parent;
    private final Vector2 pos;

    protected Widget(Screen parent, Vector2 pos) {
        this.parent = parent;
        this.pos = pos;
    }

    public Screen getParent() {
        return parent;
    }

    public Vector2 getPos() {
        return pos;
    }
}
