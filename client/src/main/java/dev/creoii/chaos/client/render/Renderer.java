package dev.creoii.chaos.client.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.creoii.chaos.client.ClientGame;
import dev.creoii.chaos.client.render.screen.Screen;
import dev.creoii.chaos.client.util.Renderable;

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
    private final ArrayMap<RenderSpace, ArrayMap<RenderLayer, List<Renderable>>> renderables;
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

        renderables = new ArrayMap<>(RenderSpace.values().length - 1);

        for (RenderSpace space : RenderSpace.values()) {
            renderables.put(space, new ArrayMap<>());
        }

        for (RenderLayer layer : RenderLayer.values()) {
            renderables.get(layer.getSpace()).put(layer, new ArrayList<>());
        }

        registerRenderable(RenderLayer.ENTITY, game.getEntityManager());
        registerRenderable(RenderLayer.HUD, new HudRenderer());
    }

    public void registerRenderable(RenderLayer renderLayer, Renderable renderable) {
        renderables.get(renderLayer.getSpace()).get(renderLayer).add(renderable);
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

    public void render(float delta, boolean debug) {
        if (game.getCharacter() == null)
            return;

        updateCameraSeek();

        for (RenderSpace space : RenderSpace.values()) {
            space.setup(viewport, batch, shapeRenderer, camera);

            renderables.get(space).forEach(entry -> entry.value.forEach(renderable -> {
                batch.begin();
                renderable.render(this, batch, null, font, delta, debug);
                batch.end();
                renderable.render(this, null, shapeRenderer, font, delta, debug);
            }));
        }

        if (currentScreen != null) {
            batch.begin();
            currentScreen.render(this, batch, null, font, delta, debug);
            batch.end();

            currentScreen.render(this, null, shapeRenderer, font, delta, debug);
        }
    }

    private void updateCameraSeek() {
        Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        Vector2 direction = new Vector2(mousePos.x - game.getCharacter().x, mousePos.y - game.getCharacter().y);
        if (direction.len2() > 1e-4f)
            direction.nor().scl(CAMERA_LOOK_OFFSET);

        camera.position.x += ((game.getCharacter().x + direction.x) - camera.position.x) * 0.2f;
        camera.position.y += ((game.getCharacter().y + direction.y) - camera.position.y) * 0.2f;
        camera.update();
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
