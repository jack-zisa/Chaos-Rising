package dev.creoii.chaos.server;

import com.badlogic.gdx.maps.tiled.TiledMap;
import dev.creoii.chaos.World;
import dev.creoii.chaos.network.NetworkQueue;
import dev.creoii.chaos.network.s2c.SetTileS2C;
import dev.creoii.chaos.network.s2c.SetTilesS2C;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerWorld implements World {
    private static final Random RANDOM = new Random();
    private final ServerGame game;
    private final ServerWorldListener listener;
    protected NetworkQueue<NetworkQueue.QueuedPacket> networkQueue;
    private final TiledMap map;
    private final ServerEntityManager entityManager;
    private final CollisionManager collisionManager;

    public ServerWorld(ServerGame game, TiledMap map) {
        this.game = game;
        this.map = map;
        networkQueue = new NetworkQueue<>(null, new ConcurrentLinkedQueue<>());
        entityManager = new ServerEntityManager(this);
        collisionManager = new CollisionManager(this);

        game.getServer().addListener(listener = new ServerWorldListener(this));
    }

    @Override
    public TiledMap getMap() {
        return map;
    }

    @Override
    public ServerGame getGame() {
        return game;
    }

    @Override
    public Random getRandom() {
        return RANDOM;
    }

    @Override
    public ServerEntityManager getEntityManager() {
        return entityManager;
    }

    public CollisionManager getCollisionManager() {
        return collisionManager;
    }

    @Override
    public void setGround(int x, int y, String tile) {
        game.getServer().sendToAllTCP(new SetTileS2C(LAYER_GROUND, x, y, tile));
    }

    @Override
    public void setGroundArea(int x1, int y1, int x2, int y2, String tile) {
        game.getServer().sendToAllTCP(new SetTilesS2C(LAYER_GROUND, x1, y1, x2, y2, tile));
    }

    @Override
    public void setObject(int x, int y, String tile) {
        game.getServer().sendToAllTCP(new SetTileS2C(LAYER_OBJECT, x, y, tile));
    }

    @Override
    public void setObjectArea(int x1, int y1, int x2, int y2, String tile) {
        game.getServer().sendToAllTCP(new SetTilesS2C(LAYER_OBJECT, x1, y1, x2, y2, tile));
    }

    @Override
    public void dispose() {
        map.dispose();
    }

    public void update() {
        collisionManager.checkCollisions();

        NetworkQueue.QueuedPacket packet;
        while ((packet = networkQueue.queue().poll()) != null) {
            listener.handlePacket(packet.connection(), packet.packet());
        }
    }
}
