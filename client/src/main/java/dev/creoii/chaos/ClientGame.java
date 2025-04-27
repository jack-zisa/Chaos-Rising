package dev.creoii.chaos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.esotericsoftware.kryonet.Client;
import dev.creoii.chaos.entity.CharacterEntity;
import dev.creoii.chaos.input.InputManager;
import dev.creoii.chaos.network.Networking;
import dev.creoii.chaos.render.Renderer;
import dev.creoii.chaos.texture.TextureManager;

import java.io.IOException;

public class ClientGame extends ApplicationAdapter implements Game, Disposable {
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    private final Client client;
    private Renderer renderer;
    private TextureManager textureManager;
    private final OptionsManager optionsManager;
    private final InputManager inputManager;
    private final CommandManager commandManager;
    private CharacterEntity character;
    private boolean debug;

    public ClientGame() throws IOException {
        client = new Client();

        optionsManager = new OptionsManager();
        inputManager = new InputManager(this);
        commandManager = new CommandManager(this);
    }

    @Override
    public void create() {
        renderer = new Renderer(this);
        textureManager = new TextureManager();

        Networking.register(client.getKryo());
        client.addListener(new ClientListener(this));
        client.start();

        try {
            client.connect(5000, "localhost", 54555, 54777);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        textureManager.load();

        Gdx.input.setInputProcessor(new InputMultiplexer(commandManager, inputManager));
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);

        commandManager.update();
        inputManager.update();

        renderer.render(debug);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        textureManager.dispose();
    }

    public Client getClient() {
        return client;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public TextureManager getTextureManager() {
        return textureManager;
    }

    @Override
    public OptionsManager getOptionsManager() {
        return optionsManager;
    }

    @Override
    public DataManager getDataManager() {
        return null;
    }

    @Override
    public EntityManager getEntityManager() {
        return null;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public CharacterEntity getCharacter() {
        return character;
    }

    public void setCharacter(CharacterEntity character) {
        this.character = character;
    }

    @Override
    public int getGametime() {
        return 0;
    }

    public boolean getDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
