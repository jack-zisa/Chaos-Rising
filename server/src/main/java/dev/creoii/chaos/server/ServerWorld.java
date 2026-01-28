package dev.creoii.chaos.server;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import dev.creoii.chaos.World;
import dev.creoii.chaos.network.NetworkQueue;
import dev.creoii.chaos.network.s2c.PlaceSetpieceS2C;
import dev.creoii.chaos.network.s2c.SetTileS2C;
import dev.creoii.chaos.network.s2c.SetTilesS2C;
import dev.creoii.chaos.server.util.ServerTiledMapTile;
import dev.creoii.chaos.world.map.MapGenerator;
import dev.creoii.chaos.world.setpiece.Setpiece;
import dev.creoii.chaos.world.tile.Tile;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerWorld implements World {
    private final long seed;
    private final Random random;
    private final ServerGame game;
    private final ServerWorldListener listener;
    protected NetworkQueue<NetworkQueue.QueuedPacket> networkQueue;
    private final TiledMap map;
    private final ServerEntityManager entityManager;
    private final CollisionManager collisionManager;

    public ServerWorld(ServerGame game, TiledMap map) {
        this.game = game;
        this.map = map;
        seed = game.getRandom().nextLong();
        random = new Random(seed);
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
    public long getSeed() {
        return seed;
    }

    @Override
    public Random getRandom() {
        return random;
    }

    @Override
    public ServerEntityManager getEntityManager() {
        return entityManager;
    }

    public CollisionManager getCollisionManager() {
        return collisionManager;
    }

    @Override
    public void setGround(int x, int y, Tile tile) {
        game.getServer().sendToAllTCP(new SetTileS2C(LAYER_GROUND, x, y, tile.id()));

        MapLayer mapLayer = getLayer(LAYER_GROUND);
        if (mapLayer instanceof TiledMapTileLayer tiledMapTileLayer) {
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            ServerTiledMapTile tiledMapTile = new ServerTiledMapTile();
            tiledMapTile.getProperties().put("id", tile.id());
            cell.setTile(tiledMapTile);
            tiledMapTileLayer.setCell(x, y, cell);
        }
    }

    @Override
    public void setGroundArea(int x1, int y1, int x2, int y2, Tile tile) {
        game.getServer().sendToAllTCP(new SetTilesS2C(LAYER_GROUND, x1, y1, x2, y2, tile.id()));

        MapLayer mapLayer = getLayer(LAYER_GROUND);
        if (mapLayer instanceof TiledMapTileLayer tiledMapTileLayer) {
            for (int x = x1; x <= x2; ++x) {
                for (int y = y1; y <= y2; ++y) {
                    TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                    ServerTiledMapTile tiledMapTile = new ServerTiledMapTile();
                    tiledMapTile.getProperties().put("id", tile.id());
                    cell.setTile(tiledMapTile);
                    tiledMapTileLayer.setCell(x, y, cell);
                }
            }
        }
    }

    @Override
    public void setObject(int x, int y, Tile tile) {
        game.getServer().sendToAllTCP(new SetTileS2C(LAYER_OBJECT, x, y, tile.id()));

        MapLayer mapLayer = getLayer(LAYER_OBJECT);
        if (mapLayer instanceof TiledMapTileLayer tiledMapTileLayer) {
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            ServerTiledMapTile tiledMapTile = new ServerTiledMapTile();
            tiledMapTile.getProperties().put("id", tile.id());
            cell.setTile(tiledMapTile);
            tiledMapTileLayer.setCell(x, y, cell);
        }
    }

    @Override
    public void setObjectArea(int x1, int y1, int x2, int y2, Tile tile) {
        game.getServer().sendToAllTCP(new SetTilesS2C(LAYER_OBJECT, x1, y1, x2, y2, tile.id()));

        MapLayer mapLayer = getLayer(LAYER_OBJECT);
        if (mapLayer instanceof TiledMapTileLayer tiledMapTileLayer) {
            for (int x = x1; x <= x2; ++x) {
                for (int y = y1; y <= y2; ++y) {
                    TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                    ServerTiledMapTile tiledMapTile = new ServerTiledMapTile();
                    tiledMapTile.getProperties().put("id", tile.id());
                    cell.setTile(tiledMapTile);
                    tiledMapTileLayer.setCell(x, y, cell);
                }
            }
        }
    }

    @Override
    public void placeSetpiece(Setpiece setpiece, int x, int y) {
        setpiece.place(this, x, y);
        World.super.placeSetpiece(setpiece, x, y);
        game.getServer().sendToAllTCP(new PlaceSetpieceS2C(setpiece.id(), x, y));
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

    public void load(MapGenerator map) {
        map.place(this);
    }
}
