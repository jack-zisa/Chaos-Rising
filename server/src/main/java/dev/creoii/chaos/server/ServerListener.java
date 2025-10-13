package dev.creoii.chaos.server;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.chat.Message;
import dev.creoii.chaos.network.NetworkQueue;
import dev.creoii.chaos.network.c2s.*;
import dev.creoii.chaos.network.s2c.*;
import dev.creoii.chaos.server.chat.command.Command;
import dev.creoii.chaos.server.chat.command.Commands;
import dev.creoii.chaos.entity.CharacterEntity;
import dev.creoii.chaos.entity.CharacterEntityType;
import dev.creoii.chaos.entity.LootDropEntity;
import dev.creoii.chaos.inventory.*;
import dev.creoii.chaos.item.AbilityItem;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.item.WeaponItem;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.Mutable;
import dev.creoii.chaos.util.provider.vecprovider.ConstantVecProvider;
import dev.creoii.chaos.util.provider.vecprovider.SourcePosVecProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ServerListener extends Listener {
    private final ServerGame game;

    public ServerListener(ServerGame game) {
        this.game = game;
    }

    @Override
    public void connected(Connection connection) {
        ServerGame.LOGGER.info("Client connected: " + connection.getRemoteAddressTCP());

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
            try (Stream<Path> paths = Files.walk(dataRoot)) {
                for (Path path : (Iterable<Path>) paths.filter(Files::isRegularFile)::iterator) {
                    ZipEntry entry = new ZipEntry(
                        dataRoot.relativize(path).toString().replace("\\", "/")
                    );
                    zipOut.putNextEntry(entry);
                    Files.copy(path, zipOut);
                    zipOut.closeEntry();
                }
            }
            zipOut.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        game.getServer().sendToTCP(connection.getID(), new SyncDataS2C(baos.toByteArray()));
    }

    @Override
    public void received(Connection connection, Object object) {
        game.networkQueue.queue().add(new NetworkQueue.QueuedPacket(connection, object));
    }

    public void handlePacket(Connection connection, Object object) {
        if (object instanceof CharacterMoveC2S(int id, float dx, float dy)) {
            CharacterEntity character = (CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, id);
            if (character != null) {
                character.setPrevPos(character.getPos().x, character.getPos().y);
                Vector2 newPos = character.getPos().add(new Vector2(dx, dy).nor().scl(character.getStats().speed().value() / 8f));
                game.getServer().sendToAllTCP(new MoveEntityS2C(id, newPos.x, newPos.y, newPos.x - character.getPrevPos().x, newPos.y - character.getPrevPos().y));
            }
        }

        else if (object instanceof AttackC2S(int id, Slot slot, float mouseX, float mouseY)) {
            CharacterEntity character = (CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, id);
            if (character != null) {
                ItemStack stack = slot.getStack();
                if (stack.getItem() instanceof WeaponItem weaponItem) {
                    weaponItem.getAttack().attack(new ConstantVecProvider(mouseX, mouseY), new SourcePosVecProvider(), character, weaponItem);
                }
            }
        }

        else if (object instanceof UseItemC2S(int id, Slot slot)) {
            CharacterEntity character = (CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, id);
            if (character != null) {
                ItemStack stack = slot.getStack();
                if (stack.getItem() instanceof AbilityItem abilityItem) {
                    abilityItem.getAttack().attack(ConstantVecProvider.ZERO, new SourcePosVecProvider(), character, abilityItem);
                }
            }
        }

        else if (object instanceof ChatMessageSendC2S(Message message)) {
            game.getServer().sendToAllExceptTCP(connection.getID(), new ChatMessageReceiveS2C(message));
        }

        else if (object instanceof SlotUpdateC2S(int id, SlotUpdateC2S.Action action, InventoryType from, InventoryType to, Slot fromSlot, Slot toSlot)) {
            CharacterEntity character = (CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, id);

            if (character != null) {
                LootDropEntity lootDrop = (LootDropEntity) game.getEntityManager().getEntity(character.getLootId());

                Inventory fromInventory = null;
                if (from == InventoryType.MAIN) {
                    fromInventory = character.getInventory();
                } else if (lootDrop != null) {
                    fromInventory = lootDrop.getInventory();
                }

                Inventory toInventory = null;
                if (to == InventoryType.MAIN) {
                    toInventory = character.getInventory();
                } else if (lootDrop != null) {
                    toInventory = lootDrop.getInventory();
                }

                if (fromInventory != null && toInventory != null) {
                    toInventory.updateSlot(action, fromInventory, toInventory, fromInventory.getSlot(fromSlot.getR(), fromSlot.getC()), toInventory.getSlot(toSlot.getR(), toSlot.getC()));
                }
            }
        }

        else if (object instanceof LootDropCloseC2S(int id)) {
            CharacterEntity character = (CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, id);
            if (character != null)
                character.setLootId(-1);
        }

        else if (object instanceof DropSlotItemC2S(int id, Slot slot)) {
            CharacterEntity character = (CharacterEntity) game.getEntityManager().getEntity(EntityGroup.CHARACTER, id);
            if (character != null) {
                Slot slot1 = character.getInventory().getSlot(slot.getR(), slot.getC());
                ItemStack dragCopy = slot1.getStack().copy();
                character.dropItem(dragCopy);
                character.getInventory().onRemoveItemFromSlot(slot1, slot1.getStack());
            }
        }

        else if (object instanceof ExecuteCommandC2S(int id, String commandType, String[] args)) {
            Command.Result result = Commands.tryExecute(game, id, commandType, args);
            if (result != null) {
                game.getServer().sendToAllTCP(new ChatMessageReceiveS2C(new Message(result.getResultMessage(commandType, args), Command.Result.getChatMessageColor(result))));
            }
        }

        else if (object instanceof CharacterJoinC2S()) {
            /*game.getEntityManager().getAllEntities().forEach((_, map) -> map.forEach((id1, entity) -> {
                game.getServer().sendToTCP(connection.getID(), new EntitySpawnS2C(id1, entity.getPos().x, entity.getPos().y, entity.getCustomPacketData()));
                game.getServer().sendToTCP(connection.getID(), new EntityDisplayS2C(id1, entity.getType().id(), entity.getType().scale()));
            }));*/

            List<SpawnEntitiesS2C.Entry> spawnEntries = new ArrayList<>();
            List<DisplayEntitiesS2C.Entry> displayEntries = new ArrayList<>();
            game.getEntityManager().getAllEntities().values().forEach(uuidEntityMap -> uuidEntityMap.values().forEach(entity -> {
                if (entity.getType().group() == EntityGroup.CHARACTER)
                    return;

                spawnEntries.add(new SpawnEntitiesS2C.Entry(entity.getId(), entity.getPos().x, entity.getPos().y, entity.getCustomPacketData()));
                displayEntries.add(new DisplayEntitiesS2C.Entry(entity.getId(), entity.getType().id(), entity.getType().scale()));
            }));

            if (!spawnEntries.isEmpty()) {
                if (spawnEntries.size() != displayEntries.size())
                    ServerGame.LOGGER.info("Length of SpawnEntities entries should equal DisplayEntities entries: " + spawnEntries.size() + " != " + displayEntries.size());

                for (int i = 0; i < spawnEntries.size(); i += 50) {
                    game.getServer().sendToTCP(connection.getID(), new SpawnEntitiesS2C(spawnEntries.subList(i, Math.min(i + 50, spawnEntries.size()))));
                    game.getServer().sendToTCP(connection.getID(), new DisplayEntitiesS2C(displayEntries.subList(i, Math.min(i + 50, displayEntries.size()))));
                }
            }

            Map<String, Object> customData = new HashMap<>();
            customData.put("connection_id", connection.getID());
            game.getEntityManager().addEntity(new CharacterEntityType(new Mutable<>(DataManager.getCharacterClass("wizard"))), new Vector2(0, 0), customData);
        }

        else if (object instanceof CharacterLeaveC2S(int id)) {
            System.out.println("character leave");
            game.getEntityManager().removeEntity(id);
        }
    }

    @Override
    public void disconnected(Connection connection) {
        ServerGame.LOGGER.info("Client disconnected: " + connection.getID());
    }
}
