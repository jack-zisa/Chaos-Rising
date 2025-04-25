package dev.creoii.chaos;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import dev.creoii.chaos.chat.Commands;
import dev.creoii.chaos.entity.CharacterEntityType;
import dev.creoii.chaos.network.packet.c2s.CharacterJoinC2S;
import dev.creoii.chaos.network.packet.c2s.ExecuteCommandC2S;
import dev.creoii.chaos.network.packet.c2s.KeyInputC2S;
import dev.creoii.chaos.network.packet.c2s.MouseInputC2S;
import dev.creoii.chaos.util.Mutable;

import java.util.UUID;

public class ServerListener extends Listener {
    private final ServerGame game;

    public ServerListener(ServerGame game) {
        this.game = game;
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof CharacterJoinC2S(UUID uuid)) {
            game.getEntityManager().addEntity(uuid, new CharacterEntityType(new Mutable<>(game.getDataManager().getCharacterClass("wizard"))), new Vector2(0, 0));
        }

        else if (object instanceof ExecuteCommandC2S(UUID uuid, String commandType, String[] args)) {
            Commands.tryExecute(game, uuid, commandType, args);
        }

        else if (object instanceof KeyInputC2S(UUID uuid, int keydown, int keyheld, int keyup)) {

        }

        else if (object instanceof MouseInputC2S(UUID uuid,int screenX, int screenY)) {

        }

        super.received(connection, object);
    }
}
