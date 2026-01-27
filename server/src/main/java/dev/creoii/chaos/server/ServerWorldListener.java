package dev.creoii.chaos.server;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.chat.Message;
import dev.creoii.chaos.entity.CharacterEntity;
import dev.creoii.chaos.entity.CharacterEntityType;
import dev.creoii.chaos.entity.LootDropEntity;
import dev.creoii.chaos.inventory.Inventory;
import dev.creoii.chaos.inventory.InventoryType;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.item.AbilityItem;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.item.WeaponItem;
import dev.creoii.chaos.network.NetworkQueue;
import dev.creoii.chaos.network.c2s.*;
import dev.creoii.chaos.network.s2c.ChatMessageReceiveS2C;
import dev.creoii.chaos.network.s2c.MoveEntityS2C;
import dev.creoii.chaos.network.s2c.SpawnEntitiesS2C;
import dev.creoii.chaos.server.chat.command.Command;
import dev.creoii.chaos.server.chat.command.Commands;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.Mutable;
import dev.creoii.chaos.util.event.ExecuteCommandEvent;
import dev.creoii.chaos.util.event.MessageChatEvent;
import dev.creoii.chaos.util.provider.vecprovider.ConstantVecProvider;
import dev.creoii.chaos.util.provider.vecprovider.SourceVecProvider;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

public class ServerWorldListener extends Listener {
    private final ServerWorld world;

    public ServerWorldListener(ServerWorld world) {
        this.world = world;
    }

    @Override
    public void received(Connection connection, Object object) {
        world.networkQueue.queue().add(new NetworkQueue.QueuedPacket(connection, object));
    }

    public void handlePacket(Connection connection, Object object) {
        if (object instanceof CharacterMoveC2S(int id, boolean axis, boolean positive)) {
            CharacterEntity character = (CharacterEntity) world.getEntityManager().getEntity(EntityGroup.CHARACTER, id);
            if (character != null) {
                character.setPrevPos(character.getPos().x, character.getPos().y);
                float x = (axis ? positive ? 1f : -1f : 0f) * (character.getStats().speed().value() / 8f);
                float y = (axis ? 0f : positive ? 1f : -1f) * (character.getStats().speed().value() / 8f);
                Vector2 newPos = character.getPos().add(x, y);
                world.getGame().getServer().sendToAllExceptUDP(connection.getID(), new MoveEntityS2C(id, newPos.x, newPos.y, newPos.x - character.getPrevPos().x, newPos.y - character.getPrevPos().y));
            }
        }

        else if (object instanceof AttackC2S(int id, Slot slot, float mouseX, float mouseY)) {
            CharacterEntity character = (CharacterEntity) world.getEntityManager().getEntity(EntityGroup.CHARACTER, id);
            if (character != null) {
                ItemStack stack = slot.getStack();
                if (stack.getItem() instanceof WeaponItem weaponItem) {
                    weaponItem.getAttack().attack(new ConstantVecProvider(mouseX, mouseY), SourceVecProvider.INSTANCE, character, weaponItem);
                }
            }
        }

        else if (object instanceof UseItemC2S(int id, Slot slot)) {
            CharacterEntity character = (CharacterEntity) world.getEntityManager().getEntity(EntityGroup.CHARACTER, id);
            if (character != null) {
                ItemStack stack = slot.getStack();
                if (stack.getItem() instanceof AbilityItem abilityItem) {
                    abilityItem.getAttack().attack(ConstantVecProvider.ZERO, SourceVecProvider.INSTANCE, character, abilityItem);
                }
            }
        }

        else if (object instanceof ChatMessageSendC2S(Message message)) {
            world.getGame().getServer().sendToAllExceptTCP(connection.getID(), new ChatMessageReceiveS2C(message));
            MessageChatEvent.EVENT.invoker().onMessageChat(world, message);
        }

        else if (object instanceof SlotUpdateC2S(int id, SlotUpdateC2S.Action action, InventoryType from, InventoryType to, Slot fromSlot, Slot toSlot)) {
            CharacterEntity character = (CharacterEntity) world.getEntityManager().getEntity(EntityGroup.CHARACTER, id);

            if (character != null) {
                LootDropEntity lootDrop = (LootDropEntity) world.getEntityManager().getEntity(EntityGroup.LOOT_DROP, character.getLootId());

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
            CharacterEntity character = (CharacterEntity) world.getEntityManager().getEntity(EntityGroup.CHARACTER, id);
            if (character != null)
                character.setLootId(-1);
        }

        else if (object instanceof DropSlotItemC2S(int id, Slot slot)) {
            CharacterEntity character = (CharacterEntity) world.getEntityManager().getEntity(EntityGroup.CHARACTER, id);
            if (character != null) {
                Slot slot1 = character.getInventory().getSlot(slot.getR(), slot.getC());
                ItemStack dragCopy = slot1.getStack().copy();
                character.dropItem(dragCopy);
                character.getInventory().onRemoveItemFromSlot(slot1, slot1.getStack());
            }
        }

        else if (object instanceof ExecuteCommandC2S(int id, String commandType, String[] args)) {
            ExecuteCommandEvent.EVENT.invoker().onExecuteCommand(world, id, commandType, args);
            Command.Result result = Commands.tryExecute(world, id, commandType, args);
            if (result != null) {
                world.getGame().getServer().sendToAllTCP(new ChatMessageReceiveS2C(new Message(result.getResultMessage(commandType, args), Command.Result.getChatMessageColor(result))));
            }
        }

        else if (object instanceof CharacterJoinC2S()) {
            Object2ObjectArrayMap<String, Object> customData = new Object2ObjectArrayMap<>();
            customData.put("connection_id", connection.getID());
            CharacterEntity character = world.getEntityManager().addCharacter(connection.getID(), new CharacterEntityType(new Mutable<>(DataManager.getCharacterClass("wizard"))), new Vector2(0, 0), customData);

            ObjectList<SpawnEntitiesS2C.Entry> spawnEntries = new ObjectArrayList<>();
            world.getEntityManager().getAllEntities().values().forEach(uuidEntityMap -> uuidEntityMap.values().forEach(entity -> {
                if (entity.getId() != character.getId()) {
                    spawnEntries.add(new SpawnEntitiesS2C.Entry(entity.getId(), entity.getPos().x, entity.getPos().y, entity.getType().scale(), entity.getCustomPacketData()));
                }
            }));

            if (!spawnEntries.isEmpty()) {
                int size = spawnEntries.size();
                for (int i = 0; i < size; i += 50) {
                    world.getGame().getServer().sendToTCP(connection.getID(), new SpawnEntitiesS2C(spawnEntries.subList(i, Math.min(i + 50, spawnEntries.size()))));
                }
            }
        }

        else if (object instanceof RequestWorldLoadC2S()) {
            world.setGroundArea(0, 0, 100, 100, "grass");
        }

        else if (object instanceof CharacterLeaveC2S(int id)) {
            world.getEntityManager().removeEntity(id);
        }
    }
}
