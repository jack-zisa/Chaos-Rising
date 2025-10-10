package dev.creoii.chaos.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import dev.creoii.chaos.DataManager;
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
import java.util.Arrays;
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
                        game.setCharacter(character);
                        game.getInputManager().addInput(new CharacterController(character));
                        game.getEntityManager().addEntity(id, character);
                    }
                    case LOOT_DROP -> {
                        LootDropData lootDropData = (LootDropData) customData;
                        Optional<List<List<Slot>>> slots = lootDropData.slots();
                        game.getEntityManager().addEntity(id, new LootDropEntityRenderData(id, x, y, 0f, 0f, group.name().toLowerCase(), 32f, slots.map(Slot::toSlotArray).orElse(Slot.createEmptySlotArray(2, 4))));
                    }
                };
            }
            case EntityDisplayS2C(int id, String textureId, float scale) -> {
                EntityRenderData entityRenderData = game.getEntityManager().getEntityData(id);
                if (entityRenderData != null) {
                    entityRenderData.textureId = textureId;
                    entityRenderData.scale = scale;
                }
            }
            case MoveEntitiesS2C(List<MoveEntitiesS2C.Entry> entries) -> entries.forEach(entry -> {
                if (entry.id() != game.getCharacter().id) {
                    EntityRenderData entityRenderData = game.getEntityManager().getEntityData(entry.id());
                    float[] unpacked = MoveEntitiesS2C.unpack(entry.data());
                    if (entityRenderData != null) {
                        entityRenderData.x = unpacked[0];
                        entityRenderData.y = unpacked[1];
                        entityRenderData.xv = unpacked[2];
                        entityRenderData.yv = unpacked[3];
                    } else {
                        CharacterEntityRenderData character = game.getCharacter();
                        if (character != null) {
                            character.x = unpacked[0];
                            character.y = unpacked[1];
                            character.xv = unpacked[2];
                            character.yv = unpacked[3];
                        }
                    }
                }
            });
            case MoveCharacterS2C(byte[] data) -> {
                CharacterEntityRenderData character = game.getCharacter();
                if (character != null) {
                    float[] unpacked = MoveEntitiesS2C.unpack(data);
                    System.out.println(Arrays.toString(unpacked));
                    character.x = unpacked[0];
                    character.y = unpacked[1];
                    character.xv = unpacked[2];
                    character.yv = unpacked[3];
                }
            }
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
                    System.out.println("[Client] Client failed to sync data: " + e);
                }

                DataManager.load(cacheRoot);
            }
            default -> System.out.println("Unhandled packet type: " + object.getClass().getSimpleName());
        }
    }

    @Override
    public void disconnected(Connection connection) {
        if (game.getCharacter() != null) {
            game.getClient().sendTCP(new CharacterLeaveC2S(game.getCharacter().id));
            System.out.println("[Client] Client disconnected: " + connection.getID());
        }
    }
}
