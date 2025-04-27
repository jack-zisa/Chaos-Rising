package dev.creoii.chaos;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import dev.creoii.chaos.chat.Commands;
import dev.creoii.chaos.entity.CharacterEntityType;
import dev.creoii.chaos.entity.character.ServerCharacterEntity;
import dev.creoii.chaos.entity.controller.CharacterController;
import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.network.packet.c2s.*;
import dev.creoii.chaos.network.packet.s2c.CharacterSpawnS2C;
import dev.creoii.chaos.util.Mutable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerListener extends Listener {
    private final ServerGame game;

    public ServerListener(ServerGame game) {
        this.game = game;
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof KeyInputC2S(UUID uuid, KeyInputC2S.Action action, int keycode)) {
            ServerCharacterEntity character = game.getEntityManager().getCharacter(uuid);
            ((CharacterController) character.getController()).onKey(action, keycode);

            game.getServer().sendToTCP(connection.getID(), new CharacterSpawnS2C(uuid, character.getType().textureId(), character.getPos().x, character.getPos().y, character.getType().scale()));
        }

        else if (object instanceof MouseInputC2S(UUID uuid, MouseInputC2S.Action action, int screenX, int screenY)) {
            ServerCharacterEntity character = game.getEntityManager().getCharacter(uuid);
            ((CharacterController) character.getController()).onMouse(action, screenX, screenY);
        }

        else if (object instanceof SlotUpdateC2S(UUID uuid, SlotUpdateC2S.Action action, Inventory from, Inventory to, Slot fromSlot, Slot toSlot)) {
            ServerCharacterEntity character = game.getEntityManager().getCharacter(uuid);
            character.getInventory().updateSlot(action, from, to, fromSlot, toSlot);
        }

        else if (object instanceof LootDropCloseC2S(UUID uuid)) {
            ServerCharacterEntity character = game.getEntityManager().getCharacter(uuid);
            character.clearLootUuid();
        }

        else if (object instanceof DropSlotItemC2S(UUID uuid, Slot slot)) {
            ItemStack dragCopy = slot.getStack().copy();
            ServerCharacterEntity character = game.getEntityManager().getCharacter(uuid);
            character.dropItem(dragCopy);
            character.getInventory().onRemoveItemFromSlot(slot, dragCopy);
        }

        else if (object instanceof ExecuteCommandC2S(UUID uuid, String commandType, String[] args)) {
            Commands.tryExecute(game, uuid, commandType, args);
        }

        else if (object instanceof CharacterJoinC2S(UUID uuid)) {
            System.out.println("character join");
            Map<String, Object> data = new HashMap<>();
            data.put("connection_id", connection.getID());
            ServerCharacterEntity character = game.getEntityManager().addEntity(uuid, new CharacterEntityType(new Mutable<>(game.getDataManager().getCharacterClass("wizard"))), new Vector2(0, 0), data);
            game.getServer().sendToTCP(connection.getID(), new CharacterSpawnS2C(uuid, character.getType().textureId(), character.getPos().x, character.getPos().y, character.getType().scale()));
        }

        else if (object instanceof CharacterLeaveC2S(UUID uuid)) {
            game.getEntityManager().removeEntity(uuid);
        }
    }
}
