package dev.creoii.chaos.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.chat.Message;
import dev.creoii.chaos.client.render.entity.data.*;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.entity.serialization.*;
import dev.creoii.chaos.client.input.CharacterController;
import dev.creoii.chaos.inventory.InventoryType;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.network.NetworkQueue;
import dev.creoii.chaos.network.c2s.CharacterJoinC2S;
import dev.creoii.chaos.network.c2s.CharacterLeaveC2S;
import dev.creoii.chaos.network.s2c.*;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.stat.Stat;

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
            case EntitySpawnS2C(int id, float x, float y, EntityCustomData customData) -> {
                EntityGroup group = customData.getGroup();
                switch (group) {
                    case BULLET -> {
                        BulletData bulletData = (BulletData) customData;
                        game.getEntityManager().addEntity(id, new BulletEntityRenderData(id, x, y, 0f, 0f, group.name().toLowerCase(), 32f, bulletData.xd(), bulletData.yd()));
                    }
                    case ENEMY -> {
                        EnemyData enemyData = (EnemyData) customData;
                        game.getEntityManager().addEntity(id, new LivingEntityRenderData(id, EntityGroup.ENEMY, x, y, 0f, 0f, group.name().toLowerCase(), 32f, enemyData.baseStats(), enemyData.maxStats()));
                    }
                    case CHARACTER -> {
                        CharacterData characterData = (CharacterData) customData;
                        Optional<List<List<Slot>>> slots = characterData.slots();
                        CharacterEntityRenderData character = new CharacterEntityRenderData(id, x, y, 0f, 0f, "wizard", 32f, characterData.baseStats(), characterData.maxStats(), slots.map(Slot::toSlotArray).orElse(Slot.createEmptySlotArray(3, 4, (r, c) -> {
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
                        game.getInputManager().addInput(new CharacterController(character));
                        game.getEntityManager().addEntity(id, character);
                    }
                    case LOOT_DROP -> {
                        LootDropData lootDropData = (LootDropData) customData;
                        Optional<List<List<Slot>>> slots = lootDropData.slots();
                        game.getEntityManager().addEntity(id, new LootDropEntityRenderData(id, x, y, 0f, 0f, group.name().toLowerCase(), 32f, slots.map(Slot::toSlotArray).orElse(Slot.createEmptySlotArray(2, 4))));
                    }
                }
            }
            case SpawnEntitiesS2C(List<SpawnEntitiesS2C.Entry> entries) -> entries.forEach(entry -> {
                int id = entry.id();
                EntityRenderData renderData = game.getEntityManager().getEntityData(id);
                if (renderData != null) {
                    float[] unpacked = SpawnEntitiesS2C.unpack(entry.data());
                    float x = unpacked[0];
                    float y = unpacked[1];
                    EntityGroup group = entry.customData().getGroup();
                    switch (group) {
                        case BULLET -> {
                            BulletData bulletData = (BulletData) entry.customData();
                            game.getEntityManager().addEntity(id, new BulletEntityRenderData(id, x, y, 0f, 0f, group.name().toLowerCase(), 32f, bulletData.xd(), bulletData.yd()));
                        }
                        case ENEMY -> {
                            EnemyData enemyData = (EnemyData) entry.customData();
                            game.getEntityManager().addEntity(id, new LivingEntityRenderData(id, EntityGroup.ENEMY, x, y, 0f, 0f, group.name().toLowerCase(), 32f, enemyData.baseStats(), enemyData.maxStats()));
                        }
                        case CHARACTER -> {
                            CharacterData characterData = (CharacterData) entry.customData();
                            Optional<List<List<Slot>>> slots = characterData.slots();
                            CharacterEntityRenderData character = new CharacterEntityRenderData(id, x, y, 0f, 0f, "wizard", 32f, characterData.baseStats(), characterData.maxStats(), slots.map(Slot::toSlotArray).orElse(Slot.createEmptySlotArray(3, 4, (r, c) -> {
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
                            game.getInputManager().addInput(new CharacterController(character));
                            game.getEntityManager().addEntity(id, character);
                        }
                        case LOOT_DROP -> {
                            LootDropData lootDropData = (LootDropData) entry.customData();
                            Optional<List<List<Slot>>> slots = lootDropData.slots();
                            game.getEntityManager().addEntity(id, new LootDropEntityRenderData(id, x, y, 0f, 0f, group.name().toLowerCase(), 32f, slots.map(Slot::toSlotArray).orElse(Slot.createEmptySlotArray(2, 4))));
                        }
                    }
                }
            });
            case EntityDisplayS2C(int id, String textureId, float scale) -> {
                EntityRenderData entityRenderData = game.getEntityManager().getEntityData(id);
                if (entityRenderData != null) {
                    entityRenderData.textureId = textureId;
                    entityRenderData.scale = scale;
                }
            }
            case DisplayEntitiesS2C(List<DisplayEntitiesS2C.Entry> entries) -> entries.forEach(entry -> {
                EntityRenderData entityRenderData = game.getEntityManager().getEntityData(entry.id());
                if (entityRenderData != null) {
                    entityRenderData.textureId = entry.textureId();
                    entityRenderData.scale = entry.scale();
                }
            });
            case MoveEntitiesS2C(List<MoveEntitiesS2C.Entry> entries) -> entries.forEach(entry -> {
                EntityRenderData entityRenderData = game.getEntityManager().getEntityData(entry.id());
                if (entityRenderData != null) {
                    entityRenderData.x = entry.x();
                    entityRenderData.y = entry.y();
                    entityRenderData.xv = entry.xv();
                    entityRenderData.yv = entry.yv();
                }
            });
            case MoveEntityS2C(int id, float x, float y, float xv, float yv) -> {
                EntityRenderData entityRenderData = game.getEntityManager().getEntityData(id);
                if (entityRenderData != null) {
                    entityRenderData.x = x;
                    entityRenderData.y = y;
                    entityRenderData.xv = xv;
                    entityRenderData.yv = yv;
                }
            }
            case ChatMessageReceiveS2C(Message message) -> game.getChatManager().getMessages().add(message);
            case EntityRemoveS2C(int id) -> game.getEntityManager().removeEntity(id);
            case StatusEffectS2C(int id, StatusEffect statusEffect) -> {
                //((LivingEntity) game.getEntityManager().getEntityData(uuid)).addStatusEffect(statusEffect);
            }
            case InventoryUpdateS2C(InventoryType type, List<Slot> slots) -> {
                for (Slot slot : slots) {
                    if (type == InventoryType.MAIN) {
                        game.getCharacter().slots[slot.getR()][slot.getC()] = slot;
                    }
                }
            }
            case LivingStatUpdateS2C(Stat stat) -> {
                CharacterEntityRenderData character = game.getCharacter();
                switch (stat.type()) {
                    case HEALTH -> {
                        character.statContainer.setHealth(stat.value());
                        character.maxStatContainer.setHealth(stat.value());
                    }
                    case SPEED -> {
                        character.statContainer.setSpeed(stat.value());
                        character.maxStatContainer.setSpeed(stat.value());
                    }
                    case ATTACK_SPEED -> {
                        character.statContainer.setAttackSpeed(stat.value());
                        character.maxStatContainer.setAttackSpeed(stat.value());
                    }
                    case DEFENSE -> {
                        character.statContainer.setDefense(stat.value());
                        character.maxStatContainer.setDefense(stat.value());
                    }
                    case ATTACK -> {
                        character.statContainer.setAttack(stat.value());
                        character.maxStatContainer.setAttack(stat.value());
                    }
                    case VITALITY -> {
                        character.statContainer.setVitality(stat.value());
                        character.maxStatContainer.setVitality(stat.value());
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
                    ClientGame.LOGGER.error("Client failed to sync data: " + e);
                }

                DataManager.load(cacheRoot);
            }
            default -> ClientGame.LOGGER.error("Unhandled packet type: " + object.getClass().getSimpleName());
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
