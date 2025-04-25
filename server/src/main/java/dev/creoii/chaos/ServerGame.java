package dev.creoii.chaos;

import com.esotericsoftware.kryonet.Server;
import dev.creoii.chaos.network.Networking;

import java.io.IOException;

public class ServerGame  {
    private final Server server;
    private final Main main;
    private final DataManager dataManager;
    private final TickManager tickManager;
    private final CollisionManager collisionManager;
    private final EntityManager entityManager;
    private int gametime;

    public ServerGame(Main main) throws IOException {
        server = new Server();
        server.start();
        server.bind(54555, 54777);

        Networking.register(server.getKryo());

        this.main = main;
        dataManager = new DataManager(main);
        tickManager = new TickManager(main);
        collisionManager = new CollisionManager(main);
        entityManager = new EntityManager(main);

        server.addListener(new ServerListener(this));
    }

    public void run(float delta) {
        ++gametime;

        tickManager.tick(gametime, delta);
        collisionManager.checkCollisions();
    }

    public Server getServer() {
        return server;
    }

    public Main getMain() {
        return main;
    }

    public DataManager getDataManager() {
        return dataManager;
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

    public int getGametime() {
        return gametime;
    }
}
