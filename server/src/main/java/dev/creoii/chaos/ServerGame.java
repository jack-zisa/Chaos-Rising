package dev.creoii.chaos;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Server;
import dev.creoii.chaos.network.Networking;

import java.io.IOException;

public class ServerGame implements Game {
    private final Server server;
    private final DataManager dataManager;
    private final OptionsManager optionsManager;
    private final TickManager tickManager;
    private final CollisionManager collisionManager;
    private final EntityManager entityManager;
    private int gametime;

    public ServerGame() throws IOException {
        server = new Server();
        server.start();
        server.bind(54555, 54777);

        Networking.register(server.getKryo());

        server.addListener(new ServerListener(this));

        dataManager = new DataManager();
        optionsManager = new OptionsManager();
        tickManager = new TickManager(this);
        collisionManager = new CollisionManager(this);
        entityManager = new EntityManager(this);

        dataManager.load();
    }

    public void update() {
        ++gametime;

        tickManager.tick(gametime, Gdx.graphics.getDeltaTime());
        collisionManager.checkCollisions();
    }

    public Server getServer() {
        return server;
    }

    @Override
    public DataManager getDataManager() {
        return dataManager;
    }

    @Override
    public OptionsManager getOptionsManager() {
        return optionsManager;
    }

    public TickManager getTickManager() {
        return tickManager;
    }

    public CollisionManager getCollisionManager() {
        return collisionManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public int getGametime() {
        return gametime;
    }
}
