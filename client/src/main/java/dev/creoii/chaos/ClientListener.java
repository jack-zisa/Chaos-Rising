package dev.creoii.chaos;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.entity.serialization.*;
import dev.creoii.chaos.input.CharacterController;
import dev.creoii.chaos.inventory.InventoryType;
import dev.creoii.chaos.inventory.SlotEntry;
import dev.creoii.chaos.network.packet.c2s.CharacterJoinC2S;
import dev.creoii.chaos.network.packet.c2s.CharacterLeaveC2S;
import dev.creoii.chaos.network.packet.s2c.*;
import dev.creoii.chaos.render.data.*;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.stat.Stat;

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
        game.getClient().sendTCP(new CharacterJoinC2S(UUID.randomUUID()));
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof FrameworkMessage.KeepAlive) {
            return;
        }
        switch (object) {
            case EntitySpawnS2C(UUID uuid, float x, float y, EntityCustomData customData) -> {
                EntityGroup group = customData.getGroup();
                switch (group) {
                    case BULLET -> {
                        BulletData bulletData = (BulletData) customData;
                        game.getEntityManager().addEntity(uuid, new BulletEntityRenderData(uuid, x, y, 0f, 0f, group.name().toLowerCase(), 32f, bulletData.xd(), bulletData.yd()));
                    }
                    case ENEMY -> {
                        EnemyData enemyData = (EnemyData) customData;
                        game.getEntityManager().addEntity(uuid, new LivingEntityRenderData(uuid, EntityGroup.ENEMY, x, y, 0f, 0f, group.name().toLowerCase(), 32f, enemyData.baseStats(), enemyData.maxStats()));
                    }
                    case CHARACTER -> {
                        CharacterData characterData = (CharacterData) customData;
                        CharacterEntityRenderData character = new CharacterEntityRenderData(uuid, x, y, 0f, 0f, "wizard", 32f, characterData.baseStats(), characterData.maxStats(), SlotRenderData.fromSlotEntryList(characterData.slots()));
                        game.setCharacter(character);
                        game.getEntityManager().addEntity(uuid, character);
                        game.getInputManager().addInput(new CharacterController(character));
                    }
                    case LOOT_DROP -> {
                        LootDropData lootDropData = (LootDropData) customData;
                        game.getEntityManager().addEntity(uuid, new LootDropEntityRenderData(uuid, x, y, 0f, 0f, group.name().toLowerCase(), 32f, SlotRenderData.fromSlotEntryList(lootDropData.slots())));
                    }
                    case null, default -> throw new IllegalArgumentException("Unknown EntityGroup: " + (group == null ? "null" : group.name().toLowerCase()));
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
            case LivingStatUpdateS2C(Stat.Type statType, int value) -> {
                CharacterEntityRenderData character = game.getCharacter();
                switch (statType) {
                    case HEALTH -> {
                        character.statContainer.setHealth(value);
                        character.maxStatContainer.setHealth(value);
                    }
                    case SPEED -> {
                        character.statContainer.setSpeed(value);
                        character.maxStatContainer.setSpeed(value);
                    }
                    case ATTACK_SPEED -> {
                        character.statContainer.setAttackSpeed(value);
                        character.maxStatContainer.setAttackSpeed(value);
                    }
                    case DEFENSE -> {
                        character.statContainer.setDefense(value);
                        character.maxStatContainer.setDefense(value);
                    }
                    case ATTACK -> {
                        character.statContainer.setAttack(value);
                        character.maxStatContainer.setAttack(value);
                    }
                    case VITALITY -> {
                        character.statContainer.setVitality(value);
                        character.maxStatContainer.setVitality(value);
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

                DataManager.load(cacheRoot);
            }
            default -> System.out.println("Unhandled packet type: " + object.getClass().getSimpleName());
        }
    }

    @Override
    public void disconnected(Connection connection) {
        if (game.getCharacter() != null) {
            game.getClient().sendTCP(new CharacterLeaveC2S(game.getCharacter().uuid));
            System.out.println("[Client] Client disconnected: " + connection.getID());
        }
    }
}
