package dev.creoii.chaos.render;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.util.Renderable;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private final Main main;
    private final List<Renderable> renderables;

    public Renderer(Main main) {
        this.main = main;
        renderables = new ArrayList<>();
        renderables.add(new EntityRenderer());
        renderables.add(new HudRenderer());
    }

    public Main getMain() {
        return main;
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        shapeRenderer.begin();
        renderables.forEach(renderable -> renderable.render(this, null, shapeRenderer, font, debug));
        shapeRenderer.end();

        batch.begin();
        renderables.forEach(renderable -> renderable.render(this, batch, null, font, debug));
        batch.end();
    }
}
