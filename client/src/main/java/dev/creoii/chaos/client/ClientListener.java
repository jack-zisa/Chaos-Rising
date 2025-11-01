package dev.creoii.chaos.client;

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.chat.Message;
import dev.creoii.chaos.client.render.entity.data.*;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.entity.BulletEntityType;
import dev.creoii.chaos.entity.serialization.*;
import dev.creoii.chaos.client.input.CharacterController;
import dev.creoii.chaos.inventory.InventoryType;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.network.NetworkQueue;
import dev.creoii.chaos.network.c2s.CharacterJoinC2S;
import dev.creoii.chaos.network.c2s.CharacterLeaveC2S;
import dev.creoii.chaos.network.s2c.*;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.event.ChangeStatEvent;
import dev.creoii.chaos.util.event.DamageEntityEvent;
import dev.creoii.chaos.util.provider.numberprovider.ConstantNumberProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;
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
            case EntitySpawnS2C(int id, float x, float y, float scale, EntityCustomData customData) -> {
                EntityGroup group = customData.getGroup();
                switch (group) {
                    case BULLET -> {
                        BulletData bulletData = (BulletData) customData;

                        NumberProvider angleOffset;

                        BulletEntityType bulletEntityType = DataManager.getBullet(bulletData.textureId());
                        if (bulletEntityType != null) {
                            angleOffset = bulletEntityType.angleOffset();
                        } else angleOffset = ConstantNumberProvider.ZERO;

                        game.getEntityManager().addEntity(id, new BulletEntityRenderData(id, x, y, 0f, 0f, bulletData.textureId(), scale, bulletData.xd(), bulletData.yd(), angleOffset));
                    }
                    case ENEMY -> {
                        EnemyData enemyData = (EnemyData) customData;
                        game.getEntityManager().addEntity(id, new LivingEntityRenderData(id, EntityGroup.ENEMY, x, y, 0f, 0f, enemyData.textureId(), scale, enemyData.baseStats(), enemyData.maxStats()));
                    }
                    case CHARACTER -> {
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
                        game.getEntityManager().addEntity(id, character);
                    }
                    case LOOT_DROP -> {
                        LootDropData lootDropData = (LootDropData) customData;
                        Optional<List<List<Slot>>> slots = lootDropData.slots();
                        game.getEntityManager().addEntity(id, new LootDropEntityRenderData(id, x, y, 0f, 0f, lootDropData.textureId(), scale, slots.map(Slot::toSlotArray).orElse(Slot.createEmptySlotArray(2, 4))));
                    }
                }
            }
            case SpawnEntitiesS2C(List<SpawnEntitiesS2C.Entry> entries) -> entries.forEach(entry -> {
                int id = entry.id();
                float x = entry.x();
                float y = entry.y();
                float scale = entry.scale();
                EntityGroup group = entry.customData().getGroup();
                switch (group) {
                    case BULLET -> {
                        BulletData bulletData = (BulletData) entry.customData();

                        NumberProvider angleOffset;

                        BulletEntityType bulletEntityType = DataManager.getBullet(bulletData.textureId());
                        if (bulletEntityType != null) {
                            angleOffset = bulletEntityType.angleOffset();
                        } else angleOffset = ConstantNumberProvider.ZERO;

                        game.getEntityManager().addEntity(id, new BulletEntityRenderData(id, x, y, 0f, 0f, bulletData.textureId(), scale, bulletData.xd(), bulletData.yd(), angleOffset));
                    }
                    case ENEMY -> {
                        EnemyData enemyData = (EnemyData) entry.customData();
                        game.getEntityManager().addEntity(id, new LivingEntityRenderData(id, EntityGroup.ENEMY, x, y, 0f, 0f, enemyData.textureId(), scale, enemyData.baseStats(), enemyData.maxStats()));
                    }
                    case CHARACTER -> {
                        CharacterData characterData = (CharacterData) entry.customData();
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
                        game.getEntityManager().addEntity(id, character);
                    }
                    case LOOT_DROP -> {
                        LootDropData lootDropData = (LootDropData) entry.customData();
                        Optional<List<List<Slot>>> slots = lootDropData.slots();
                        game.getEntityManager().addEntity(id, new LootDropEntityRenderData(id, x, y, 0f, 0f, lootDropData.textureId(), scale, slots.map(Slot::toSlotArray).orElse(Slot.createEmptySlotArray(2, 4))));
                    }
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
            case EntityDamageS2C(int id, float amount) -> {
                EntityRenderData entityRenderData = game.getEntityManager().getEntityData(id);
                if (entityRenderData != null) {
                    if (entityRenderData instanceof LivingEntityRenderData livingEntityRenderData) {
                        livingEntityRenderData.statContainer.setHealth((int) (livingEntityRenderData.statContainer.health().value() - amount));
                    }
                    DamageEntityEvent.EVENT.invoker().onDamageEntity(game, amount, id, -1);
                    game.getRenderer().getStatusTextManager().addStatusText(String.valueOf(amount), entityRenderData.x + (entityRenderData.scale / 2f), entityRenderData.y + entityRenderData.scale, 20, Color.RED);
                }
            }
            case ChatMessageReceiveS2C(Message message) -> game.getChatManager().getMessages().add(message);
            case EntityRemoveS2C(int id) -> game.getEntityManager().removeEntity(id);
            case RemoveEntitiesS2C(List<Integer> ids) -> ids.forEach(integer -> game.getEntityManager().removeEntity(integer));
            case StatusEffectS2C(int id, StatusEffect statusEffect) -> {
                //((LivingEntity) game.getEntityManager().getEntityData(uuid)).addStatusEffect(statusEffect);
            }
            case InventoryUpdateS2C(int id, InventoryType type, List<Slot> slots) -> {
                EntityRenderData entityRenderData = game.getEntityManager().getEntityData(id);
                if (entityRenderData instanceof CharacterEntityRenderData character) {
                    for (Slot slot : slots) {
                        if (type == InventoryType.MAIN) {
                            character.slots[slot.getR()][slot.getC()] = slot;
                        }
                    }
                } else if (entityRenderData instanceof LootDropEntityRenderData lootDrop) {
                    for (Slot slot : slots) {
                        if (type == InventoryType.MAIN) {
                            lootDrop.slots[slot.getR()][slot.getC()] = slot;
                        }
                    }
                }
            }
            case LivingStatUpdateS2C(int id, Stat stat, boolean setMax) -> {
                EntityRenderData renderData = game.getEntityManager().getEntityData(id);
                if (renderData instanceof LivingEntityRenderData livingEntityRenderData) {
                    switch (stat.type()) {
                        case HEALTH -> {
                            livingEntityRenderData.statContainer.setHealth(stat.value());
                            if (setMax)
                                livingEntityRenderData.maxStatContainer.setHealth(stat.value());
                        }
                        case SPEED -> {
                            livingEntityRenderData.statContainer.setSpeed(stat.value());
                            if (setMax)
                                livingEntityRenderData.maxStatContainer.setSpeed(stat.value());
                        }
                        case ATTACK_SPEED -> {
                            livingEntityRenderData.statContainer.setAttackSpeed(stat.value());
                            if (setMax)
                                livingEntityRenderData.maxStatContainer.setAttackSpeed(stat.value());
                        }
                        case DEFENSE -> {
                            livingEntityRenderData.statContainer.setDefense(stat.value());
                            if (setMax)
                                livingEntityRenderData.maxStatContainer.setDefense(stat.value());
                        }
                        case ATTACK -> {
                            livingEntityRenderData.statContainer.setAttack(stat.value());
                            if (setMax)
                                livingEntityRenderData.maxStatContainer.setAttack(stat.value());
                        }
                        case VITALITY -> {
                            livingEntityRenderData.statContainer.setVitality(stat.value());
                            if (setMax)
                                livingEntityRenderData.maxStatContainer.setVitality(stat.value());
                        }
                    }

                    ChangeStatEvent.EVENT.invoker().onChangeStat(game, id, stat);
                }
            }

        /*else if (object instanceof LootDropOpenS2C(UUID uuid)) {
            game.getCharacter().setLootUuid(uuid);
        }

        else if (object instanceof LootDropCloseS2C()) {
            game.getCharacter().setLootUuid(null);
        }*/
            case CharacterJoinS2C(int id, float x, float y, float scale, EntityCustomData customData) -> {
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
                    game.getEntityManager().addEntity(id, character);
                    game.getInputManager().addInput(new CharacterController());
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
