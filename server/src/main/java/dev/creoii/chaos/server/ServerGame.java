package dev.creoii.chaos.server;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.World;
import dev.creoii.chaos.network.NetworkQueue;
import dev.creoii.chaos.network.CreoSerialization;
import dev.creoii.chaos.network.Networking;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.logging.Logger;
import dev.creoii.chaos.world.map.LayeredMapGenerator;
import dev.creoii.chaos.world.map.MapGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class ServerGame implements Game {
    private final Server server;
    private final ServerListener listener;
    private static final Random RANDOM = new Random();
    public static final Logger LOGGER = new Logger(ServerGame.class.getSimpleName());
    protected NetworkQueue<NetworkQueue.QueuedPacket> networkQueue;
    private final TickManager tickManager;
    private final Map<String, World> worlds;
    private int gametime;

    public ServerGame(int tcpPort, int udpPort) throws IOException {
        server = new Server(256 * 1024, 256 * 1024, new CreoSerialization());
        networkQueue = new NetworkQueue<>(null, new ConcurrentLinkedQueue<>());
        Log.NONE();
        server.start();
        server.bind(tcpPort, udpPort);

        LOGGER.info("Server started on ports: TCP " + tcpPort + " | UDP " + udpPort);

        Networking.register(server.getKryo());

        server.addListener(listener = new ServerListener(this));

        ComponentTypes.init();
        DataManager.load();

        tickManager = new TickManager();
        worlds = new HashMap<>();

        MapGenerator mapGenerator = DataManager.getMapGenerator("test_dungeon");
        if (mapGenerator == null)
            mapGenerator = LayeredMapGenerator.DEFAULT;
        worlds.put("main", new ServerWorld(this, mapGenerator));

        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        LOGGER.info("Active Threads:");
        threadSet.forEach(thread -> LOGGER.info("    " + thread.getName()));

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

        NetworkQueue.QueuedPacket packet;
        while ((packet = networkQueue.queue().poll()) != null) {
            listener.handlePacket(packet.connection(), packet.packet());
        }

        worlds.forEach((_, world) -> ((ServerWorld) world).update());
    }

    @Override
    public Random getRandom() {
        return RANDOM;
    }

    @Override
    public Server getServer() {
        return server;
    }

    public TickManager getTickManager() {
        return tickManager;
    }

    public Map<String, World> getWorlds() {
        return worlds;
    }

    @Override
    public int getGametime() {
        return gametime;
    }
}
