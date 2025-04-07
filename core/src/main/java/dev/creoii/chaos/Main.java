package dev.creoii.chaos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.creoii.chaos.render.Renderer;

public class Main extends ApplicationAdapter {
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    private Game game;
    private Renderer renderer;
    private boolean debug;
    private SpriteBatch batch;
    private FitViewport viewport;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(WINDOW_WIDTH, WINDOW_HEIGHT);

        renderer = new Renderer(this);
        game = new Game(this);

        font = new BitmapFont();
        font.setUseIntegerPositions(false);
        font.getData().setScale(2f);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        game.init();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);

        batch.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
    }

    @Override
    public void render() {
        viewport.apply();
        ScreenUtils.clear(Color.BLACK);

        float delta = Gdx.graphics.getDeltaTime();
        game.run(delta);

        renderer.render(batch, shapeRenderer, font, debug);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        game.dispose();
    }

    public Game getGame() {
        return game;
    }

    public boolean getDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
