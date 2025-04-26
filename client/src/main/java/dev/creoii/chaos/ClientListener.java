package dev.creoii.chaos;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.entity.ClientBulletEntity;
import dev.creoii.chaos.entity.ClientCharacterEntity;
import dev.creoii.chaos.entity.ClientEntity;
import dev.creoii.chaos.entity.ClientLivingEntity;
import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.network.packet.c2s.CharacterJoinC2S;
import dev.creoii.chaos.network.packet.c2s.CharacterLeaveC2S;
import dev.creoii.chaos.network.packet.s2c.*;
import dev.creoii.chaos.util.EntityGroup;

import java.util.UUID;

public class ClientListener extends Listener {
    private final ClientGame game;

    public ClientListener(ClientGame game) {
        this.game = game;
    }

    @Override
    public void connected(Connection connection) {
        game.getClient().sendTCP(new CharacterJoinC2S(Constants.TEST_CHARACTER_UUID));
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof EntityStateS2C(UUID uuid, float x, float y)) {
            game.getMain().getRenderer().getEntityRenderManager().updateEntity(uuid, x, y);
        }

        else if (object instanceof EntitySpawnS2C(UUID uuid, EntityGroup group, String textureId, float x, float y, float scale)) {
            if (group == EntityGroup.OTHER)
                game.getMain().getRenderer().getEntityRenderManager().addEntity(new ClientEntity(game, uuid, textureId, x, y, scale));
            else if (group == EntityGroup.BULLET)
                game.getMain().getRenderer().getEntityRenderManager().addEntity(new ClientBulletEntity(game, uuid, textureId, x, y, scale, 0f, 0f));
            else if (group == EntityGroup.ENEMY)
                game.getMain().getRenderer().getEntityRenderManager().addEntity(new ClientLivingEntity(game, uuid, textureId, x, y, scale));
        }

        else if (object instanceof EntityRemoveS2C(UUID uuid)) {
            game.getMain().getRenderer().getEntityRenderManager().removeEntity(uuid);
        }

        else if (object instanceof StatusEffectS2C(UUID uuid, StatusEffect statusEffect)) {
            ((ClientLivingEntity) game.getMain().getRenderer().getEntityRenderManager().getEntity(uuid)).addStatusEffect(statusEffect);
        }

        else if (object instanceof LootDropOpenS2C(Inventory inventory)) {
            game.getCharacter().setLootInventory(inventory);
        }

        else if (object instanceof LootDropCloseS2C()) {
            game.getCharacter().clearLootInventory();
        }

        else if (object instanceof CharacterSpawnS2C(UUID uuid, String textureId, float x, float y, float scale)) {
            System.out.println("spawn character");
            game.setCharacter(new ClientCharacterEntity(game, uuid, textureId, x, y, scale, "wizard", new Inventory(3, 4)));
            game.getMain().getRenderer().getEntityRenderManager().addEntity(game.getCharacter());
        }
    }

    @Override
    public void disconnected(Connection connection) {
        game.getClient().sendTCP(new CharacterLeaveC2S(game.getCharacter().getUuid()));
    }
}
