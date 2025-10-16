package dev.creoii.chaos.server;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.network.NetworkQueue;
import dev.creoii.chaos.network.CreoSerialization;
import dev.creoii.chaos.network.Networking;
import dev.creoii.chaos.util.logging.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class ServerGame implements Game {
    private final Server server;
    private final ServerListener listener;
    public static final Logger LOGGER = new Logger(ServerGame.class.getSimpleName());
    protected NetworkQueue<NetworkQueue.QueuedPacket> networkQueue;
    private final TickManager tickManager;
    private final CollisionManager collisionManager;
    private final ServerEntityManager entityManager;
    private int gametime;

    public ServerGame(int tcpPort, int udpPort) throws IOException, URISyntaxException {
        server = new Server(65536, 65536, new CreoSerialization());
        networkQueue = new NetworkQueue<>(null, new ConcurrentLinkedQueue<>());
        Log.NONE();
        server.start();
        server.bind(tcpPort, udpPort);

        LOGGER.info("Server started on ports: TCP " + tcpPort + " | UDP " + udpPort);

        Networking.register(server.getKryo());

        server.addListener(listener = new ServerListener(this));

        tickManager = new TickManager();
        collisionManager = new CollisionManager(this);
        entityManager = new ServerEntityManager(this);

        URL baseUrl = getClass().getClassLoader().getResource("data");
        if (baseUrl == null) {
            DataManager.LOGGER.error("Directory 'data/' does not exist");
            return;
        }

        DataManager.load(Paths.get(baseUrl.toURI()));

        long nextTick = System.nanoTime();
        long tickInterval = 50_000_000L;

        while (true) {
            update();

            nextTick += tickInterval;
            long sleepNanos = nextTick - System.nanoTime();

            if (sleepNanos > 0) {
                try {
                    TimeUnit.NANOSECONDS.sleep(sleepNanos);
                } catch (InterruptedException e) {
                    LOGGER.error("Thread sleep interrupted: " + e);
                }
            } else {
                LOGGER.info("Falling " + (-sleepNanos) + " ns behind");
                nextTick = System.nanoTime();
            }
        }
    }

    @Override
    public boolean isClient() {
        return false;
    }

    public void update() {
        tickManager.tick(++gametime, 1f);
        collisionManager.checkCollisions();

        NetworkQueue.QueuedPacket packet;
        while ((packet = networkQueue.queue().poll()) != null) {
            listener.handlePacket(packet.connection(), packet.packet());
        }
    }

    @Override
    public Server getServer() {
        return server;
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

    @Override
    public int getGametime() {
        return gametime;
    }
}
