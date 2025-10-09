package dev.creoii.chaos.server;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.OptionsManager;
import dev.creoii.chaos.server.inventory.cooldown.CooldownManager;
import dev.creoii.chaos.network.CreoSerialization;
import dev.creoii.chaos.network.Networking;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class ServerGame implements Game {
    private final Server server;
    private final OptionsManager optionsManager;
    private final TickManager tickManager;
    private final CollisionManager collisionManager;
    private final ServerEntityManager entityManager;
    private final CooldownManager cooldownManager;
    private int gametime;

    public ServerGame(int tcpPort, int udpPort) throws IOException, URISyntaxException {
        server = new Server(65536, 65536, new CreoSerialization());
        Log.NONE();
        server.start();
        server.bind(tcpPort, udpPort);

        System.out.println("[Server] Server started on ports: TCP " + tcpPort + " | UDP " + udpPort);

        Networking.register(server.getKryo());

        server.addListener(new ServerListener(this));

        optionsManager = new OptionsManager();
        tickManager = new TickManager();
        collisionManager = new CollisionManager(this);
        entityManager = new ServerEntityManager(this);
        cooldownManager = new CooldownManager(this);

        URL baseUrl = getClass().getClassLoader().getResource("data");
        if (baseUrl == null) {
            System.out.println("[DataManager] Directory 'data/' does not exist");
            return;
        }

        DataManager.load(Paths.get(baseUrl.toURI()));

        while (true) {
            update();
        }
    }

    @Override
    public boolean isClient() {
        return false;
    }

    public void update() {
        tickManager.tick(++gametime, 1f);
        collisionManager.checkCollisions();
    }

    @Override
    public Server getServer() {
        return server;
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

    @Override
    public ServerEntityManager getEntityManager() {
        return entityManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    @Override
    public int getGametime() {
        return gametime;
    }
}
