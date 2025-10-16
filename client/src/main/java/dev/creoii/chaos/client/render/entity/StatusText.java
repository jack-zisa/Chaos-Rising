package dev.creoii.chaos.client.render.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Pool;
import dev.creoii.chaos.client.render.Renderer;
import dev.creoii.chaos.client.util.Renderable;

import javax.annotation.Nullable;

public class StatusText implements Renderable, Pool.Poolable {
    private String text;
    private float x;
    private float y;
    private float lifetime;
    private Color color;

    public StatusText(String text, float x, float y, float lifetime, Color color) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.lifetime = lifetime;
        this.color = color;
    }

    public StatusText() {
        this("", 0f, 0f, -1f, Color.WHITE);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setPos(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getLifetime() {
        return lifetime;
    }

    public void setLifetime(float lifetime) {
        this.lifetime = lifetime;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, float delta, boolean debug) {
        if (batch != null && lifetime > 0f) {
            font.draw(batch, text, x, y += delta * 10f);
            lifetime -= delta * 10f;
        }
    }

    @Override
    public void reset() {
        setText("");
        setLifetime(-1);
        setColor(Color.WHITE);
    }
}
