package dev.creoii.chaos;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import dev.creoii.chaos.chat.Commands;
import dev.creoii.chaos.entity.CharacterEntity;
import dev.creoii.chaos.entity.CharacterEntityType;
import dev.creoii.chaos.entity.LootDropEntity;
import dev.creoii.chaos.inventory.*;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.network.packet.c2s.*;
import dev.creoii.chaos.network.packet.s2c.CharacterSpawnS2C;
import dev.creoii.chaos.network.packet.s2c.EntityStateS2C;
import dev.creoii.chaos.network.packet.s2c.SyncDataS2C;
import dev.creoii.chaos.util.Mutable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ServerListener extends Listener {
    private final ServerGame game;

    public ServerListener(ServerGame game) {
        this.game = game;
    }

    @Override
    public void connected(Connection connection) {
        System.out.println("[Server] Client connected: " + connection.getRemoteAddressTCP());

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource("data/");
        if (url == null)
            throw new IllegalStateException("Could not find data folder");
        Path dataRoot;
        try {
            dataRoot = Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream(32768);
        try (ZipOutputStream zipOut = new ZipOutputStream(baos)) {
            Files.walk(dataRoot).filter(Files::isRegularFile).forEach(path -> {
                    try {
                        if (Files.isDirectory(path))
                            return;

                        zipOut.putNextEntry(new ZipEntry(dataRoot.relativize(path).toString().replace("\\", "/")));
                        zipOut.write(Files.readAllBytes(path));
                        zipOut.closeEntry();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            zipOut.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }

        game.getServer().sendToTCP(connection.getID(), new SyncDataS2C(baos.toByteArray()));
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof CharacterMoveC2S(UUID uuid, float dx, float dy)) {
            CharacterEntity character = game.getEntityManager().getCharacter(uuid);
            Vector2 newPos = character.getPos().add(new Vector2(dx, dy).nor().scl(character.getStats().speed.value() / 8f));
            character.setPrevPos(character.getPos().x, character.getPos().y);
            character.setPos(newPos.x, newPos.y);

            game.getServer().sendToTCP(connection.getID(), new EntityStateS2C(uuid, newPos.x, newPos.y));
        }

        else if (object instanceof SlotUpdateC2S(UUID uuid, SlotUpdateC2S.Action action, InventoryType from, InventoryType to, SlotEntry fromSlot, SlotEntry toSlot)) {
            CharacterEntity character = game.getEntityManager().getCharacter(uuid);

            Inventory fromInventory = from == InventoryType.MAIN ? character.getInventory() : ((LootDropEntity) game.getEntityManager().getEntity(character.getLootUuid())).getInventory();
            Inventory toInventory = from == InventoryType.MAIN ? character.getInventory() : ((LootDropEntity) game.getEntityManager().getEntity(character.getLootUuid())).getInventory();

            toInventory.updateSlot(action, fromInventory, toInventory, fromInventory.getSlot(fromSlot.r(), fromSlot.c()), toInventory.getSlot(toSlot.r(), toSlot.c()));
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
            Map<String, Object> customData = new HashMap<>();
            customData.put("connection_id", connection.getID());
            customData.put("inventory", new ServerCharacterInventory());
            CharacterEntity character = game.getEntityManager().addEntity(uuid, new CharacterEntityType(new Mutable<>(game.getDataManager().getCharacterClass("wizard"))), new Vector2(0, 0), customData);
            game.getServer().sendToTCP(connection.getID(), new CharacterSpawnS2C(uuid, character.getCharacterClass().get().id(), character.getPos()));
        }

        else if (object instanceof CharacterLeaveC2S(UUID uuid)) {
            game.getEntityManager().removeEntity(uuid);
        }
    }

    @Override
    public void disconnected(Connection connection) {
        System.out.println("[Server] Client disconnected: " + connection.getRemoteAddressTCP());
    }
}
