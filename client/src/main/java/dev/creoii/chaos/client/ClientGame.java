package dev.creoii.chaos.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.OptionsManager;
import dev.creoii.chaos.client.input.InputManager;
import dev.creoii.chaos.network.CreoSerialization;
import dev.creoii.chaos.network.NetworkQueue;
import dev.creoii.chaos.network.Networking;
import dev.creoii.chaos.client.render.Renderer;
import dev.creoii.chaos.client.render.entity.EntityRenderManager;
import dev.creoii.chaos.client.render.data.CharacterEntityRenderData;
import dev.creoii.chaos.client.texture.TextureManager;

import java.io.IOException;

public class ClientGame extends ApplicationAdapter implements Game, Disposable {
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    private final Client client;
    private final ClientListener listener;
    protected NetworkQueue<Object> networkQueue;
    private Renderer renderer;
    private TextureManager textureManager;
    private final OptionsManager optionsManager;
    private final EntityRenderManager entityManager;
    private final InputManager inputManager;
    private final CommandManager commandManager;
    private CharacterEntityRenderData character;
    private boolean debug;

    public ClientGame() throws IOException {
        client = new Client(32768, 32768, new CreoSerialization());
        listener = new ClientListener(this);
        optionsManager = new OptionsManager();
        entityManager = new EntityRenderManager(this);
        inputManager = new InputManager(this);
        commandManager = new CommandManager(this);
    }

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public void create() {
        renderer = new Renderer(this);
        textureManager = new TextureManager();

        Networking.register(client.getKryo());
        client.addListener(listener);
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

        Object packet;
        while ((packet = networkQueue.queue().poll()) != null) {
            listener.handlePacket(networkQueue.connection(), packet);
        }

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

    public NetworkQueue<Object> getNetworkQueue() {
        return networkQueue;
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

    public EntityRenderManager getEntityManager() {
        return entityManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public CharacterEntityRenderData getCharacter() {
        return character;
    }

    public void setCharacter(CharacterEntityRenderData character) {
        this.character = character;
    }

    @Override
    public int getGametime() {
        return 0;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @Override
    public Server getServer() {
        return null;
    }
}
