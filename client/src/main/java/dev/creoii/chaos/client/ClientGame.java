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
import dev.creoii.chaos.client.chat.ChatManager;
import dev.creoii.chaos.client.input.InputManager;
import dev.creoii.chaos.network.CreoSerialization;
import dev.creoii.chaos.network.NetworkQueue;
import dev.creoii.chaos.network.Networking;
import dev.creoii.chaos.client.render.Renderer;
import dev.creoii.chaos.client.render.entity.EntityRenderManager;
import dev.creoii.chaos.client.render.entity.data.CharacterEntityRenderData;
import dev.creoii.chaos.util.logging.Logger;

import java.io.IOException;

public class ClientGame extends ApplicationAdapter implements Game, Disposable {
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    private final Client client;
    private final ClientListener listener;
    public static final Logger LOGGER = new Logger(ClientGame.class.getSimpleName());
    protected NetworkQueue<Object> networkQueue;
    private Renderer renderer;
    private AssetManager assetManager;
    private final EntityRenderManager entityManager;
    private final InputManager inputManager;
    private final ChatManager chatManager;
    private int characterId;
    private boolean debug;

    public ClientGame() throws IOException {
        client = new Client(32768, 32768, new CreoSerialization());
        listener = new ClientListener(this);
        entityManager = new EntityRenderManager(this);
        inputManager = new InputManager(this);
        chatManager = new ChatManager(this);
    }

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public void create() {
        renderer = new Renderer(this);
        assetManager = new AssetManager();

        Networking.register(client.getKryo());
        client.addListener(listener);
        client.start();

        try {
            client.connect(5000, "localhost", 54555, 54777);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assetManager.load();

        Gdx.input.setInputProcessor(new InputMultiplexer(chatManager, inputManager, renderer.getStage()));
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

        chatManager.update();
        inputManager.update();

        renderer.render(Gdx.graphics.getDeltaTime(), debug);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        assetManager.load();
        super.resume();
    }

    @Override
    public void dispose() {
        renderer.dispose();
        assetManager.dispose();
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

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public EntityRenderManager getEntityManager() {
        return entityManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    public CharacterEntityRenderData getCharacter() {
        return (CharacterEntityRenderData) getEntityManager().getEntityData(characterId);
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
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
        LOGGER.error("Attempted to access server on client.");
        return null;
    }
}
