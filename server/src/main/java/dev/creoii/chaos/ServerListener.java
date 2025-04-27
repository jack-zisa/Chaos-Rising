package dev.creoii.chaos;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import dev.creoii.chaos.chat.Commands;
import dev.creoii.chaos.entity.CharacterEntity;
import dev.creoii.chaos.entity.CharacterEntityType;
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
        if (object instanceof CharacterStateC2S(UUID uuid, float x, float y)) {
            CharacterEntity character = game.getEntityManager().getCharacter(uuid);
            character.setPrevPos(character.getPos().x, character.getPos().y);
            character.setPos(x, y);
        }

        else if (object instanceof SlotUpdateC2S(UUID uuid, SlotUpdateC2S.Action action, Inventory from, Inventory to, Slot fromSlot, Slot toSlot)) {
            CharacterEntity character = game.getEntityManager().getCharacter(uuid);
            character.getInventory().updateSlot(action, from, to, fromSlot, toSlot);
        }

        else if (object instanceof LootDropCloseC2S(UUID uuid)) {
            CharacterEntity character = game.getEntityManager().getCharacter(uuid);
            character.setLootUuid(null);
        }

        else if (object instanceof DropSlotItemC2S(UUID uuid, Slot slot)) {
            ItemStack dragCopy = slot.getStack().copy();
            CharacterEntity character = game.getEntityManager().getCharacter(uuid);
            character.dropItem(dragCopy);
            character.getInventory().onRemoveItemFromSlot(slot, dragCopy);
        }

        else if (object instanceof ExecuteCommandC2S(UUID uuid, String commandType, String[] args)) {
            Commands.tryExecute(game, uuid, commandType, args);
        }

        else if (object instanceof CharacterJoinC2S(UUID uuid)) {
            System.out.println("character join " + connection.getID());
            Map<String, Object> data = new HashMap<>();
            data.put("connection_id", connection.getID());
            CharacterEntity character = game.getEntityManager().addEntity(uuid, new CharacterEntityType(new Mutable<>(game.getDataManager().getCharacterClass("wizard"))), new Vector2(0, 0), data);
            game.getServer().sendToTCP(connection.getID(), new CharacterSpawnS2C(character));
        }

        else if (object instanceof CharacterLeaveC2S(UUID uuid)) {
            game.getEntityManager().removeEntity(uuid);
        }
    }
}
