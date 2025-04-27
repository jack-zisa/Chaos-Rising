package dev.creoii.chaos;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.entity.*;
import dev.creoii.chaos.input.CharacterController;
import dev.creoii.chaos.network.packet.c2s.CharacterJoinC2S;
import dev.creoii.chaos.network.packet.c2s.CharacterLeaveC2S;
import dev.creoii.chaos.network.packet.s2c.*;

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
            game.getRenderer().getEntityRenderManager().updateEntity(uuid, x, y);
        }

        else if (object instanceof EntitySpawnS2C(Entity entity)) {
            game.getRenderer().getEntityRenderManager().addEntity(entity);
        }

        else if (object instanceof EntityRemoveS2C(UUID uuid)) {
            game.getRenderer().getEntityRenderManager().removeEntity(uuid);
        }

        else if (object instanceof StatusEffectS2C(UUID uuid, StatusEffect statusEffect)) {
            ((LivingEntity) game.getRenderer().getEntityRenderManager().getEntity(uuid)).addStatusEffect(statusEffect);
        }

        else if (object instanceof LootDropOpenS2C(UUID uuid)) {
            game.getCharacter().setLootUuid(uuid);
        }

        else if (object instanceof LootDropCloseS2C()) {
            game.getCharacter().setLootUuid(null);
        }

        else if (object instanceof CharacterSpawnS2C(CharacterEntity character)) {
            game.setCharacter(character);
            game.getRenderer().getEntityRenderManager().addEntity(character);
            game.getInputManager().addInput(new CharacterController(game.getCharacter()));
        }
    }

    @Override
    public void disconnected(Connection connection) {
        game.getClient().sendTCP(new CharacterLeaveC2S(game.getCharacter().getUuid()));
    }
}
