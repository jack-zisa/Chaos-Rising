package dev.creoii.chaos.client;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.client.render.entity.data.*;
import dev.creoii.chaos.client.texture.TextureManager;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.entity.serialization.*;
import dev.creoii.chaos.entity.serialization.CharacterData;
import dev.creoii.chaos.inventory.InventoryType;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.network.s2c.*;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.event.ChangeStatEvent;
import dev.creoii.chaos.util.event.DamageEntityEvent;
import dev.creoii.chaos.util.stat.Stat;
import dev.creoii.chaos.util.stat.StatContainer;
import dev.creoii.chaos.world.setpiece.Setpiece;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ClientWorldListener extends Listener {
    private final ClientWorld world;

    public ClientWorldListener(ClientWorld world) {
        this.world = world;
    }

    @Override
    public void received(Connection connection, Object object) {
        world.getNetworkQueue().queue().add(object);
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

                        float angle = MathUtils.atan2(bulletData.yd(), bulletData.xd()) * MathUtils.radiansToDegrees;

                        world.getEntityManager().addEntity(id, new BulletEntityRenderData(id, x, y, 0f, 0f, bulletData.textureId(), scale, bulletData.xd(), bulletData.yd(), angle));
                    }
                    case ENEMY -> {
                        EnemyData enemyData = (EnemyData) customData;
                        world.getEntityManager().addEntity(id, new LivingEntityRenderData(id, EntityGroup.ENEMY, x, y, 0f, 0f, enemyData.textureId(), scale, enemyData.baseStats(), enemyData.maxStats()));
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
                        world.getEntityManager().addEntity(id, character);
                    }
                    case LOOT_DROP -> {
                        LootDropData lootDropData = (LootDropData) customData;
                        Optional<List<List<Slot>>> slots = lootDropData.slots();
                        world.getEntityManager().addEntity(id, new LootDropEntityRenderData(id, x, y, 0f, 0f, lootDropData.textureId(), scale, slots.map(Slot::toSlotArray).orElse(Slot.createEmptySlotArray(2, 4))));
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

                        float angle = (MathUtils.atan2(bulletData.yd(), bulletData.xd()) * MathUtils.radiansToDegrees) + bulletData.angleOffset();

                        world.getEntityManager().addEntity(id, new BulletEntityRenderData(id, x, y, 0f, 0f, bulletData.textureId(), scale, bulletData.xd(), bulletData.yd(), angle));
                    }
                    case ENEMY -> {
                        EnemyData enemyData = (EnemyData) entry.customData();
                        world.getEntityManager().addEntity(id, new LivingEntityRenderData(id, EntityGroup.ENEMY, x, y, 0f, 0f, enemyData.textureId(), scale, enemyData.baseStats(), enemyData.maxStats()));
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
                        world.getEntityManager().addEntity(id, character);
                    }
                    case LOOT_DROP -> {
                        LootDropData lootDropData = (LootDropData) entry.customData();
                        Optional<List<List<Slot>>> slots = lootDropData.slots();
                        world.getEntityManager().addEntity(id, new LootDropEntityRenderData(id, x, y, 0f, 0f, lootDropData.textureId(), scale, slots.map(Slot::toSlotArray).orElse(Slot.createEmptySlotArray(2, 4))));
                    }
                }
            });
            case MoveEntitiesS2C(List<MoveEntitiesS2C.Entry> entries) -> entries.forEach(entry -> {
                EntityRenderData entityRenderData = world.getEntityManager().getEntityData(entry.id());
                if (entityRenderData != null) {
                    entityRenderData.x = entry.x();
                    entityRenderData.y = entry.y();
                    entityRenderData.xv = entry.xv();
                    entityRenderData.yv = entry.yv();
                }
            });
            case MoveEntityS2C(int id, float x, float y, float xv, float yv) -> {
                EntityRenderData entityRenderData = world.getEntityManager().getEntityData(id);
                if (entityRenderData != null) {
                    entityRenderData.x = x;
                    entityRenderData.y = y;
                    entityRenderData.xv = xv;
                    entityRenderData.yv = yv;
                }
            }
            case EntityDisplayS2C(int id, String textureId, float scale) -> {
                EntityRenderData entityRenderData = world.getEntityManager().getEntityData(id);
                if (entityRenderData != null) {
                    entityRenderData.textureId = textureId;
                    entityRenderData.scale = scale;
                    entityRenderData.sprite.setTexture(world.getGame().getAssetManager().getTextureManager().getTexture(TextureManager.Atlas.CHARACTER, textureId));
                }
            }
            case SyncAttacksS2C(float attacks) -> world.getGame().setAttacks(attacks / 2f); // divide by 2 for leeway
            case EntityDamageS2C(int id, float amount) -> {
                EntityRenderData entityRenderData = world.getEntityManager().getEntityData(id);
                if (entityRenderData instanceof LivingEntityRenderData livingEntityRenderData) {
                    livingEntityRenderData.statContainer.setHealth((int) (livingEntityRenderData.statContainer.health().value() - amount));
                    DamageEntityEvent.EVENT.invoker().onDamageEntity(world, amount, id, -1);
                    world.getWorldRenderer().getStatusTextManager().addStatusText(String.valueOf(amount), entityRenderData.x + (entityRenderData.scale / 2f), entityRenderData.y + entityRenderData.scale, 20, Color.RED);
                }
            }
            case EntityRemoveS2C(int id) -> world.getEntityManager().removeEntity(id);
            case RemoveEntitiesS2C(List<Integer> ids) -> ids.forEach(integer -> world.getEntityManager().removeEntity(integer));
            case SetTileS2C(String layer, int x, int y, String tile) -> {
                if (world != null) {
                    if (Objects.equals(layer, ClientWorld.LAYER_GROUND)) {
                        world.setGround(x, y, DataManager.getTile(tile));
                    } else if (Objects.equals(layer, ClientWorld.LAYER_OBJECT)) {
                        world.setObject(x, y, DataManager.getTile(tile));
                    }
                }
            }
            case SetTilesS2C(String layer, int x1, int y1, int x2, int y2, String tile) -> {
                if (world != null) {
                    if (Objects.equals(layer, ClientWorld.LAYER_GROUND)) {
                        world.setGroundArea(x1, y1, x2, y2, DataManager.getTile(tile));
                    } else if (Objects.equals(layer, ClientWorld.LAYER_OBJECT)) {
                        world.setObjectArea(x1, y1, x2, y2, DataManager.getTile(tile));
                    }
                }
            }
            case SyncWorldSectionS2C(List<SyncWorldSectionS2C.Entry> tiles) -> {
                if (world != null) {
                    tiles.forEach(entry -> world.setGround(entry.x(), entry.y(), DataManager.getTile(entry.tile())));
                }
            }
            case GainExperienceS2C(int id, int experience, int level) -> {
                EntityRenderData entityRenderData = world.getEntityManager().getEntityData(id);
                if (entityRenderData instanceof CharacterEntityRenderData characterEntityRenderData) {
                    world.getWorldRenderer().getStatusTextManager().addStatusText(String.valueOf(experience), entityRenderData.x + (entityRenderData.scale / 2f), entityRenderData.y + entityRenderData.scale, 20, Color.LIME);

                    characterEntityRenderData.experience = experience;

                    if (characterEntityRenderData.level != level) {
                        world.getWorldRenderer().getStatusTextManager().addStatusText(String.valueOf(level), entityRenderData.x + (entityRenderData.scale / 2f), entityRenderData.y + entityRenderData.scale, 20, Color.LIME);
                    }

                    characterEntityRenderData.level = level;
                }
            }
            case StatusEffectS2C(int id, StatusEffect.Instance instance, boolean add) -> {
                EntityRenderData entityRenderData = world.getEntityManager().getEntityData(id);
                if (entityRenderData instanceof LivingEntityRenderData livingEntityRenderData) {
                    if (add) {
                        livingEntityRenderData.statusEffects.add(instance);
                    } else livingEntityRenderData.statusEffects.remove(instance);
                }
            }
            case InventoryUpdateS2C(int id, InventoryType type, List<Slot> slots) -> {
                EntityRenderData entityRenderData = world.getEntityManager().getEntityData(id);
                if (entityRenderData instanceof CharacterEntityRenderData character) {
                    for (Slot slot : slots) {
                        if (type == InventoryType.MAIN) {
                            character.slots[slot.getR()][slot.getC()] = slot;
                        }
                    }
                } else if (entityRenderData instanceof LootDropEntityRenderData lootDrop) {
                    for (Slot slot : slots) {
                        if (type == InventoryType.LOOT) {
                            lootDrop.slots[slot.getR()][slot.getC()] = slot;
                        }
                    }
                }
            }
            case SlotUpdateS2C(int id, InventoryType inventory, Slot slot) -> {
                EntityRenderData entityRenderData = world.getEntityManager().getEntityData(id);
                if (entityRenderData instanceof CharacterEntityRenderData character) {
                    character.slots[slot.getR()][slot.getC()] = slot;
                } else if (entityRenderData instanceof LootDropEntityRenderData lootDrop) {
                    lootDrop.slots[slot.getR()][slot.getC()] = slot;
                }
            }
            case LivingStatUpdateS2C(int id, Stat stat, boolean setMax) -> {
                EntityRenderData renderData = world.getEntityManager().getEntityData(id);
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

                    ChangeStatEvent.EVENT.invoker().onChangeStat(world, id, stat);
                }
            }
            case LivingStatsUpdateS2C(int id, StatContainer stats) -> {
                EntityRenderData entityRenderData = world.getEntityManager().getEntityData(id);
                if (entityRenderData instanceof LivingEntityRenderData livingEntityRenderData) {
                    livingEntityRenderData.statContainer = stats;
                    livingEntityRenderData.maxStatContainer = stats;
                }
            }
            case PlaceSetpieceS2C(String setpiece, int x, int y) -> {
                Setpiece setpiece1 = DataManager.getSetpiece(setpiece);
                if (setpiece1 != null)
                    world.placeSetpiece(setpiece1, x, y);
            }

        /*else if (object instanceof LootDropOpenS2C(UUID uuid)) {
            game.getCharacter().setLootUuid(uuid);
        }

        else if (object instanceof LootDropCloseS2C()) {
            game.getCharacter().setLootUuid(null);
        }*/
            default -> {}
        }
    }
}
