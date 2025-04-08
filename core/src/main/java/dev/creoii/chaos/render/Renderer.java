package dev.creoii.chaos.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.util.Renderable;

import java.util.ArrayList;
import java.util.List;

public class Renderer implements Disposable {
    private final Main main;
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final ShapeRenderer shapeRenderer;

    private final List<Renderable> worldRenderables;
    private final List<Renderable> screenRenderables;

    public Renderer(Main main) {
        this.main = main;
        camera = new OrthographicCamera(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        camera.setToOrtho(false);
        viewport = new FitViewport(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        batch = new SpriteBatch();

        font = new BitmapFont();
        font.setUseIntegerPositions(false);
        font.getData().setScale(2f);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        worldRenderables = new ArrayList<>();
        worldRenderables.add(new EntityRenderer());
        screenRenderables = new ArrayList<>();
        screenRenderables.add(new HudRenderer());
    }

    public Main getMain() {
        return main;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void render(boolean debug) {
        camera.position.x += (main.getGame().getActiveCharacter().getPos().x - camera.position.x) * .2f;
        camera.position.y += (main.getGame().getActiveCharacter().getPos().y - camera.position.y) * .2f;
        camera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        shapeRenderer.begin();
        worldRenderables.forEach(renderable -> renderable.render(this, null, shapeRenderer, font, debug));
        shapeRenderer.end();

        batch.begin();
        worldRenderables.forEach(renderable -> renderable.render(this, batch, null, font, debug));
        batch.end();

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        shapeRenderer.begin();
        screenRenderables.forEach(renderable -> renderable.render(this, null, shapeRenderer, font, debug));
        shapeRenderer.end();

        batch.begin();
        screenRenderables.forEach(renderable -> renderable.render(this, batch, null, font, debug));
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}
