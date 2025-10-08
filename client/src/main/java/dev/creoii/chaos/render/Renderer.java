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
import dev.creoii.chaos.ClientGame;
import dev.creoii.chaos.render.screen.Screen;
import dev.creoii.chaos.util.Renderable;

import java.util.ArrayList;
import java.util.List;

public class Renderer implements Disposable {
    private static final float CAMERA_LOOK_OFFSET = 10f;
    private final ClientGame game;
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final BitmapFont font;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;

    private final List<Renderable> worldRenderables;
    private final List<Renderable> screenRenderables;
    private Screen currentScreen = null;

    public Renderer(ClientGame game) {
        this.game = game;
        camera = new OrthographicCamera(ClientGame.WINDOW_WIDTH, ClientGame.WINDOW_HEIGHT);
        camera.setToOrtho(false);
        viewport = new FitViewport(ClientGame.WINDOW_WIDTH, ClientGame.WINDOW_HEIGHT);

        font = new BitmapFont();
        font.setUseIntegerPositions(false);
        font.getData().setScale(2f);

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        worldRenderables = new ArrayList<>();
        worldRenderables.add(game.getEntityManager());
        screenRenderables = new ArrayList<>();
        screenRenderables.add(new HudRenderer());
    }

    public ClientGame getGame() {
        return game;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public FitViewport getViewport() {
        return viewport;
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void render(boolean debug) {
        if (game.getCharacter() == null)
            return;

        Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        Vector2 direction = new Vector2(mousePos.x - game.getCharacter().x, mousePos.y - game.getCharacter().y);
        if (direction.len2() > 1e-4f)
            direction.nor().scl(CAMERA_LOOK_OFFSET);

        camera.position.x += ((game.getCharacter().x + direction.x) - camera.position.x) * 0.2f;
        camera.position.y += ((game.getCharacter().y + direction.y) - camera.position.y) * 0.2f;
        camera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        worldRenderables.forEach(renderable -> renderable.render(this, batch, null, font, debug));
        batch.end();

        worldRenderables.forEach(renderable -> renderable.render(this, null, shapeRenderer, font, debug));

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        screenRenderables.forEach(renderable -> renderable.render(this, batch, null, font, debug));
        batch.end();

        screenRenderables.forEach(renderable -> renderable.render(this, null, shapeRenderer, font, debug));

        if (currentScreen != null) {
            batch.begin();
            currentScreen.render(this, batch, null, font, debug);
            batch.end();

            currentScreen.render(this, null, shapeRenderer, font, debug);
        }
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }

    public void setCurrentScreen(Screen currentScreen) {
        this.currentScreen = currentScreen;
        this.currentScreen.open(game);
    }

    public void clearCurrentScreen() {
        currentScreen.close(game);
        currentScreen = null;
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}
