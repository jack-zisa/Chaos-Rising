package dev.creoii.chaos;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import dev.creoii.chaos.network.packet.s2c.EntityDeathS2C;
import dev.creoii.chaos.network.packet.s2c.EntitySpawnS2C;
import dev.creoii.chaos.network.packet.s2c.EntityStateS2C;
import dev.creoii.chaos.network.packet.util.EntityGroup;

import java.util.UUID;

public class ClientListener extends Listener {
    private final ClientGame game;

    public ClientListener(ClientGame game) {
        this.game = game;
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof EntityStateS2C(UUID uuid, float x, float y)) {
            game.getMain().getRenderer().getEntityRenderManager().updateEntity(uuid, x, y);
        }

        else if (object instanceof EntitySpawnS2C(UUID uuid, String textureId, EntityGroup group, float x, float y, float scale)) {
            game.getMain().getRenderer().getEntityRenderManager().addEntity(uuid, textureId, group, x, y, scale);
        }

        else if (object instanceof EntityDeathS2C(UUID uuid)) {
            game.getMain().getRenderer().getEntityRenderManager().removeEntity(uuid);
        }

        super.received(connection, object);
    }
}
