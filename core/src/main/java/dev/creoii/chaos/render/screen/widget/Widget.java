package dev.creoii.chaos.render.screen.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.render.screen.Screen;
import dev.creoii.chaos.util.Inputtable;
import dev.creoii.chaos.util.Renderable;

import java.util.ArrayList;
import java.util.List;

public abstract class Widget implements Renderable, Inputtable {
    private final Screen parent;
    private final Vector2 pos;
    private final float width;
    private final float height;

    protected Widget(Screen parent, Vector2 pos, float width, float height) {
        this.parent = parent;
        this.pos = pos;
        this.width = width;
        this.height = height;
    }

    public Screen getParent() {
        return parent;
    }

    public Vector2 getPos() {
        return pos;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isMouseOver() {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        if (mouseX >= pos.x && mouseX <= pos.x + width && mouseY >= pos.y && mouseY <= pos.y + height) {
            return true;
        }
        return false;
    }
}
