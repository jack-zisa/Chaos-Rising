package dev.creoii.chaos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;
import dev.creoii.chaos.render.Renderer;

public class Main extends ApplicationAdapter implements Disposable {
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    private Game game;
    private Renderer renderer;
    private boolean debug;

    @Override
    public void create() {
        renderer = new Renderer(this);
        game = new Game(this);
        game.init();
        renderer.initCamera();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);

        game.run(Gdx.graphics.getDeltaTime());

        renderer.render(debug);
    }

    @Override
    public void dispose() {
        game.dispose();
        renderer.dispose();
    }

    public Game getGame() {
        return game;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public boolean getDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
