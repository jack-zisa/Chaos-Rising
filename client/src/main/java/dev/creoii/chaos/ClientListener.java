package dev.creoii.chaos;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.entity.*;
import dev.creoii.chaos.input.CharacterController;
import dev.creoii.chaos.inventory.CharacterInventory;
import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.inventory.InventoryType;
import dev.creoii.chaos.inventory.SlotEntry;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.network.packet.c2s.CharacterJoinC2S;
import dev.creoii.chaos.network.packet.c2s.CharacterLeaveC2S;
import dev.creoii.chaos.network.packet.s2c.*;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.Mutable;

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
        if (object instanceof EntityStateS2C(UUID uuid, float x, float y)) {
            game.getEntityManager().updateEntity(uuid, x, y);
        }

        else if (object instanceof LivingEntityStateS2C(UUID uuid, int health, int maxHealth, int speed, int maxSpeed)) {
            game.getEntityManager().updateLivingEntity(uuid, health, maxHealth, speed, maxSpeed);
        }

        else if (object instanceof EntitySpawnS2C(EntityGroup group, UUID uuid, String id, Vector2 pos)) {
            if (group != EntityGroup.CHARACTER) {
                game.getEntityManager().addEntity(switch (group) {
                    case BULLET -> new BulletEntity(game, game.getDataManager().getBullet(id), uuid, pos, Vector2.Zero, 1, 1, 1);
                    case ENEMY -> new EnemyEntity(game, game.getDataManager().getEnemy(id), uuid, pos);
                    case LOOT_DROP -> new LootDropEntity(game, game.getDataManager().getLootDrop(id), uuid, pos, new Inventory(2, 4));
                    default -> throw new IllegalStateException("Unexpected value: " + group);
                });
            }
        }

        else if (object instanceof EntityRemoveS2C(UUID uuid)) {
            game.getEntityManager().removeEntity(uuid);
        }

        else if (object instanceof StatusEffectS2C(UUID uuid, StatusEffect statusEffect)) {
            ((LivingEntity) game.getEntityManager().getEntity(uuid)).addStatusEffect(statusEffect);
        }

        else if (object instanceof InventoryUpdateS2C(InventoryType type, List<SlotEntry> slots)) {
            for (SlotEntry entry : slots) {
                if (type == InventoryType.MAIN) {
                    game.getCharacter().getInventory().getSlots()[entry.r()][entry.c()].setStack(new ItemStack(game.getDataManager().getItem(entry.id()), entry.count()));
                }
            }
        }

        else if (object instanceof LootDropOpenS2C(UUID uuid)) {
            game.getCharacter().setLootUuid(uuid);
        }

        else if (object instanceof LootDropCloseS2C()) {
            game.getCharacter().setLootUuid(null);
        }

        else if (object instanceof CharacterSpawnS2C(UUID uuid, String classId, Vector2 pos)) {
            Mutable<CharacterClass> characterClass = new Mutable<>(game.getDataManager().getCharacterClass(classId));
            CharacterEntity character = new CharacterEntity(game, new CharacterEntityType(characterClass), uuid, pos, connection.getID(), new CharacterInventory());
            game.setCharacter(character);
            game.getEntityManager().addEntity(character);
            game.getInputManager().addInput(new CharacterController(game.getCharacter()));
        }

        else if (object instanceof SyncDataS2C(byte[] data)) {
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
    }

    @Override
    public void disconnected(Connection connection) {
        if (game.getCharacter() != null)
            game.getClient().sendTCP(new CharacterLeaveC2S(game.getCharacter().getUuid()));
    }
}
