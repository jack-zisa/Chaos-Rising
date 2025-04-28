package dev.creoii.chaos;

import com.esotericsoftware.kryonet.Server;
import dev.creoii.chaos.network.Networking;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class ServerGame implements Game {
    private final Server server;
    private final DataManager dataManager;
    private final OptionsManager optionsManager;
    private final TickManager tickManager;
    private final CollisionManager collisionManager;
    private final EntityManager entityManager;
    private int gametime;

    public ServerGame() throws IOException, URISyntaxException {
        server = new Server(32768, 32768);
        server.start();
        server.bind(54555, 54777);

        Networking.register(server.getKryo());

        server.addListener(new ServerListener(this));

        dataManager = new DataManager();
        optionsManager = new OptionsManager();
        tickManager = new TickManager();
        collisionManager = new CollisionManager(this);
        entityManager = new ServerEntityManager(this);

        URL baseUrl = getClass().getClassLoader().getResource("data");
        if (baseUrl == null) {
            System.out.println("[DataManager] Directory 'data/' does not exist.");
            return;
        }

        dataManager.load(Paths.get(baseUrl.toURI()));

        while (true) {
            update();
        }
    }

    public void update() {
        ++gametime;

        tickManager.tick(gametime, 1f);
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
