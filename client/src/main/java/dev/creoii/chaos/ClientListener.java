package dev.creoii.chaos;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.input.CharacterController;
import dev.creoii.chaos.inventory.InventoryType;
import dev.creoii.chaos.inventory.SlotEntry;
import dev.creoii.chaos.network.packet.c2s.CharacterJoinC2S;
import dev.creoii.chaos.network.packet.c2s.CharacterLeaveC2S;
import dev.creoii.chaos.network.packet.s2c.*;
import dev.creoii.chaos.render.entity.data.*;
import dev.creoii.chaos.util.EntityGroup;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
        if (object instanceof FrameworkMessage.KeepAlive) {
            return;
        }
        switch (object) {
            case EntitySpawnS2C(UUID uuid, EntityGroup group, float x, float y) -> {
                switch (group) {
                    case BULLET -> game.getEntityManager().addEntity(uuid, new BulletEntityRenderData(uuid, x, y, 0f, 0f, group.name().toLowerCase(), 32f, 0f, 0f));
                    case ENEMY -> game.getEntityManager().addEntity(uuid, new LivingEntityRenderData(uuid, EntityGroup.ENEMY, x, y, 0f, 0f, group.name().toLowerCase(), 32f));
                    case CHARACTER -> {
                        CharacterEntityRenderData character = new CharacterEntityRenderData(uuid, x, y, 0f, 0f, "wizard", 32f, new SlotRenderData[3][4]);
                        game.setCharacter(character);
                        game.getEntityManager().addEntity(uuid, character);
                        game.getInputManager().addInput(new CharacterController(character));
                    }
                    case LOOT_DROP -> game.getEntityManager().addEntity(uuid, new LootDropEntityRenderData(uuid, x, y, 0f, 0f, group.name().toLowerCase(), 32f, new SlotRenderData[2][4]));
                    case null, default -> throw new IllegalArgumentException("Unknown EntityGroup: " + (group == null ? "null" : group.name()));
                }
            }
            case EntityDisplayS2C(UUID uuid, String textureId, float scale) -> {
                EntityRenderData entityRenderData = game.getEntityManager().getEntityData(uuid);
                if (entityRenderData != null) {
                    entityRenderData.textureId = textureId;
                    entityRenderData.scale = scale;
                }
            }
            case EntityMoveS2C(UUID uuid, float x, float y, float xv, float yv) -> {
                EntityRenderData entityRenderData = game.getEntityManager().getEntityData(uuid);
                if (entityRenderData != null) {
                    entityRenderData.x = x;
                    entityRenderData.y = y;
                    entityRenderData.xv = xv;
                    entityRenderData.yv = yv;
                }
            }
            case EntityRemoveS2C(UUID uuid) -> game.getEntityManager().removeEntity(uuid);
            case StatusEffectS2C(UUID uuid, StatusEffect statusEffect) -> {
                //((LivingEntity) game.getEntityManager().getEntityData(uuid)).addStatusEffect(statusEffect);
            }
            case InventoryUpdateS2C(InventoryType type, List<SlotEntry> slots) -> {
                for (SlotEntry entry : slots) {
                    if (type == InventoryType.MAIN) {
                        game.getCharacter().slots[entry.r()][entry.c()].stack = entry.stack();
                    }
                }
            }

        /*else if (object instanceof LootDropOpenS2C(UUID uuid)) {
            game.getCharacter().setLootUuid(uuid);
        }

        else if (object instanceof LootDropCloseS2C()) {
            game.getCharacter().setLootUuid(null);
        }*/
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
                    e.printStackTrace();
                }

                game.getDataManager().load(cacheRoot);
            }
            default -> System.out.println("Unhandled packet type: " + object.getClass().getSimpleName());
        }
    }

    @Override
    public void disconnected(Connection connection) {
        if (game.getCharacter() != null)
            game.getClient().sendTCP(new CharacterLeaveC2S(game.getCharacter().uuid));
    }
}
