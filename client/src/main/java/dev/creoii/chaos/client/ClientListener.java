package dev.creoii.chaos.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.World;
import dev.creoii.chaos.chat.Message;
import dev.creoii.chaos.client.render.entity.data.*;
import dev.creoii.chaos.entity.serialization.*;
import dev.creoii.chaos.client.input.CharacterController;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.network.NetworkQueue;
import dev.creoii.chaos.network.c2s.CharacterJoinC2S;
import dev.creoii.chaos.network.c2s.CharacterLeaveC2S;
import dev.creoii.chaos.network.c2s.RequestWorldLoadC2S;
import dev.creoii.chaos.network.s2c.*;
import dev.creoii.chaos.util.EntityGroup;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ClientListener extends Listener {
    private final ClientGame game;

    public ClientListener(ClientGame game) {
        this.game = game;
    }

    @Override
    public void connected(Connection connection) {
        game.networkQueue = new NetworkQueue<>(connection);
        game.getClient().sendTCP(new CharacterJoinC2S());
    }

    @Override
    public void received(Connection connection, Object object) {
        game.getNetworkQueue().queue().add(object);
    }

    public void handlePacket(Connection connection, Object object) {
        if (object instanceof FrameworkMessage.KeepAlive) {
            return;
        }

        switch (object) {
            case ChatMessageReceiveS2C(Message message) -> game.getChatManager().getMessages().add(message);
            case CharacterJoinS2C(int id, float x, float y, float scale, EntityCustomData customData, long seed) -> {
                EntityGroup group = customData.getGroup();
                if (group == EntityGroup.CHARACTER) {
                    CharacterData characterData = (CharacterData) customData;
                    Optional<List<List<Slot>>> slots = characterData.slots();
                    CharacterEntityRenderData character = new CharacterEntityRenderData(id, x, y, 0f, 0f, characterData.textureId(), scale, characterData.baseStats(), characterData.maxStats(), slots.map(Slot::toSlotArray).orElse(Slot.createEmptySlotArray(3, 4, (r, c) -> {
                        if (r == 2) {
                            return switch (c) {
                                case 0 -> new Slot(r, c, Slot.Type.WEAPON);
                                case 1 -> new Slot(r, c, Slot.Type.ABILITY);
                                case 2 -> new Slot(r, c, Slot.Type.ARMOR);
                                default -> new Slot(r, c, Slot.Type.ACCESSORY);
                            };
                        } else return new Slot(r, c);
                    })));
                    game.setCharacterId(id);
                    game.getInputManager().addInput(new CharacterController());

                    game.setWorld(new ClientWorld(game, World.createMapOfSize(100, 100), seed));
                    game.getWorld().networkQueue = new NetworkQueue<>(connection);
                    game.getWorld().getEntityManager().addEntity(id, character);

                    connection.sendTCP(new RequestWorldLoadC2S());
                }
            }
            case SyncDataS2C(byte[] data) -> {
                Path cacheRoot = Paths.get(System.getProperty("user.dir"), "cache", "data");

                try (ZipInputStream zipIn = new ZipInputStream(new ByteArrayInputStream(data))) {
                    ZipEntry entry;
                    while ((entry = zipIn.getNextEntry()) != null) {
                        Path filePath = cacheRoot.resolve(entry.getName());
                        Files.createDirectories(filePath.getParent());
                        Files.write(filePath, zipIn.readAllBytes());
                        zipIn.closeEntry();
                    }
                } catch (IOException e) {
                    ClientGame.LOGGER.error("Client failed to sync data: " + e);
                }

                DataManager.load(cacheRoot);
            }
            default -> {}
        }
    }

    @Override
    public void disconnected(Connection connection) {
        if (game.getCharacter() != null) {
            game.getClient().sendTCP(new CharacterLeaveC2S(game.getCharacter().id));
            ClientGame.LOGGER.info("Client disconnected: " + connection.getID());
        }
    }
}
