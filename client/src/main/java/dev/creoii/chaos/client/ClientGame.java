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
import dev.creoii.chaos.client.input.InputManager;
import dev.creoii.chaos.network.CreoSerialization;
import dev.creoii.chaos.network.NetworkQueue;
import dev.creoii.chaos.network.PacketRegistry;
import dev.creoii.chaos.client.render.Renderer;
import dev.creoii.chaos.client.render.entity.data.CharacterEntityRenderData;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.logging.Logger;

import java.io.IOException;
import java.util.Random;
import java.util.Set;

public class ClientGame extends ApplicationAdapter implements Game, Disposable {
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    private final Client client;
    private final ClientListener listener;
    private static final Random RANDOM = new Random();
    public static final Logger LOGGER = new Logger(ClientGame.class.getSimpleName());
    protected NetworkQueue<Object> networkQueue;
    private Renderer renderer;
    private AssetManager assetManager;
    private final InputManager inputManager;
    private ClientWorld world;
    private int characterId = -1;
    private boolean debug = false;
    private float attacks = 0f;

    public ClientGame() throws IOException {
        client = new Client(256 * 1024, 256 * 1024, new CreoSerialization());
        listener = new ClientListener(this);
        inputManager = new InputManager(this);
    }

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public void create() {
        renderer = new Renderer(this);
        assetManager = new AssetManager();

        PacketRegistry.register(client.getKryo());
        client.addListener(listener);
        client.start();

        try {
            client.connect(5000, "localhost", 54555, 54777);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        LOGGER.info("Active Threads:");
        threadSet.forEach(thread -> LOGGER.info("    " + thread.getName()));

        ComponentTypes.init();

        assetManager.load();

        Gdx.input.setInputProcessor(new InputMultiplexer(inputManager, renderer.getStage()));
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

        if (world != null && characterId >= 0) {
            float delta = Gdx.graphics.getDeltaTime();

            world.getChatManager().update();
            inputManager.update();

            world.render(delta, renderer, debug);

            renderer.render(delta, debug);

            world.renderLight(delta, renderer, debug);
        }
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
        listener.disconnected(networkQueue.connection());

        if (client.isConnected()) {
            client.close();
        }

        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {}

        client.stop();
        try {
            client.dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        renderer.dispose();
        assetManager.dispose();
        world.dispose();
    }

    public Client getClient() {
        return client;
    }

    @Override
    public Random getRandom() {
        return RANDOM;
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

    public InputManager getInputManager() {
        return inputManager;
    }

    public ClientWorld getWorld() {
        return world;
    }

    public void setWorld(ClientWorld world) {
        this.world = world;
    }

    public CharacterEntityRenderData getCharacter() {
        return (CharacterEntityRenderData) getWorld().getEntityManager().getEntityData(EntityGroup.CHARACTER, characterId);
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

    public float getAttacks() {
        return attacks;
    }

    public void setAttacks(float attacks) {
        this.attacks = attacks;
    }

    @Override
    public Server getServer() {
        LOGGER.error("Attempted to access server on client.");
        return null;
    }
}
