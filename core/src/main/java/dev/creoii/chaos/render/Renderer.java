package dev.creoii.chaos.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.render.entity.EntityRenderManager;
import dev.creoii.chaos.render.screen.Screen;
import dev.creoii.chaos.util.Renderable;

import java.util.ArrayList;
import java.util.List;

public class Renderer implements Disposable {
    private static final float CAMERA_LOOK_OFFSET = 10f;
    private final Main main;
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final BitmapFont font;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;

    private final List<Renderable> worldRenderables;
    private final List<Renderable> screenRenderables;
    private Screen currentScreen = null;

    public Renderer(Main main) {
        this.main = main;
        camera = new OrthographicCamera(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        camera.setToOrtho(false);
        viewport = new FitViewport(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);

        font = new BitmapFont();
        font.setUseIntegerPositions(false);
        font.getData().setScale(2f);

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        worldRenderables = new ArrayList<>();
        worldRenderables.add(new EntityRenderManager(main));
        screenRenderables = new ArrayList<>();
        screenRenderables.add(new HudRenderer());

        camera.position.x = main.getGame().getActiveCharacter().getPos().x;
        camera.position.y = main.getGame().getActiveCharacter().getPos().y;
        camera.update();
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
        Vector2 playerPos = main.getGame().getActiveCharacter().getPos();
        Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        Vector2 direction = new Vector2(mousePos.x - playerPos.x, mousePos.y - playerPos.y);
        if (direction.len2() > 1e-4f)
            direction.nor().scl(CAMERA_LOOK_OFFSET);

        camera.position.x += ((playerPos.x + direction.x) - camera.position.x) * 0.2f;
        camera.position.y += ((playerPos.y + direction.y) - camera.position.y) * 0.2f;
        camera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        worldRenderables.forEach(renderable -> renderable.render(this, batch, null, font, debug));
        batch.end();

        shapeRenderer.begin();
        worldRenderables.forEach(renderable -> renderable.render(this, null, shapeRenderer, font, debug));
        shapeRenderer.end();

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        screenRenderables.forEach(renderable -> renderable.render(this, batch, null, font, debug));
        batch.end();

        shapeRenderer.begin();
        screenRenderables.forEach(renderable -> renderable.render(this, null, shapeRenderer, font, debug));
        shapeRenderer.end();

        if (currentScreen != null) {
            batch.begin();
            currentScreen.render(this, batch, null, font, debug);
            batch.end();

            shapeRenderer.begin();
            currentScreen.render(this, null, shapeRenderer, font, debug);
            shapeRenderer.end();
        }
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }

    public void setCurrentScreen(Screen currentScreen) {
        this.currentScreen = currentScreen;
        this.currentScreen.open(main);
    }

    public void clearCurrentScreen() {
        currentScreen.close(main);
        currentScreen = null;
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}
