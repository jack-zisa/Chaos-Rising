package dev.creoii.chaos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;
import dev.creoii.chaos.server.ServerLauncher;

import java.io.IOException;

public class ServerMain extends ServerLauncher implements Disposable {
    private ServerGame game;

    public ServerMain() throws IOException {
        getServer().addListener(new ServerListener(this));
    }

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
