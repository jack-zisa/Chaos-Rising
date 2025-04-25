package dev.creoii.chaos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter implements Disposable {
    private ServerGame game;

    @Override
    public void create() {
        game = new ServerGame(this);
        game.getDataManager().load();
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);

        game.run(Gdx.graphics.getDeltaTime());
    }

    public ServerGame getGame() {
        return game;
    }
}
