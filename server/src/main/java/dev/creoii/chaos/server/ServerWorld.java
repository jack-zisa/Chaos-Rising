package dev.creoii.chaos.server;

import com.badlogic.gdx.maps.tiled.TiledMap;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.World;
import dev.creoii.chaos.network.s2c.SetTileS2C;
import dev.creoii.chaos.network.s2c.SetTilesS2C;

public class ServerWorld implements World {
    private final ServerGame game;
    private final TiledMap map;

    public ServerWorld(ServerGame game, TiledMap map) {
        this.game = game;
        this.map = map;
    }

    @Override
    public TiledMap getMap() {
        return map;
    }

    @Override
    public Game getGame() {
        return game;
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
}
