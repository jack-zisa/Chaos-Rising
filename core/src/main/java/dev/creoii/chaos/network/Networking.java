package dev.creoii.chaos.network;

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import dev.creoii.chaos.attack.Attack;
import dev.creoii.chaos.chat.Message;
import dev.creoii.chaos.entity.behavior.Behavior;
import dev.creoii.chaos.entity.behavior.action.Action;
import dev.creoii.chaos.entity.behavior.phase.Phase;
import dev.creoii.chaos.entity.controller.bulletpath.BulletPath;
import dev.creoii.chaos.entity.serialization.EntityCustomData;
import dev.creoii.chaos.inventory.InventoryType;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.item.Item;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.network.c2s.*;
import dev.creoii.chaos.network.s2c.*;
import dev.creoii.chaos.util.Codecs;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.Rarity;
import dev.creoii.chaos.util.logging.Logger;
import dev.creoii.chaos.util.stat.ModifierEntry;
import dev.creoii.chaos.util.stat.StatContainer;

import java.util.UUID;

public class Networking {
    public static final Logger LOGGER = new Logger(Networking.class.getSimpleName());

    public static void register(Kryo kryo) {
        kryo.register(CharacterJoinC2S.class, CodecSerializer.INSTANCE);
        kryo.register(CharacterLeaveC2S.class, CodecSerializer.INSTANCE);
        kryo.register(CharacterMoveC2S.class, CodecSerializer.INSTANCE);
        kryo.register(DropSlotItemC2S.class, CodecSerializer.INSTANCE);
        kryo.register(ExecuteCommandC2S.class, CodecSerializer.INSTANCE);
        kryo.register(LootDropCloseC2S.class, CodecSerializer.INSTANCE);
        kryo.register(SlotUpdateC2S.class, CodecSerializer.INSTANCE);
        kryo.register(UseItemC2S.class, CodecSerializer.INSTANCE);
        kryo.register(AttackC2S.class, CodecSerializer.INSTANCE);
        kryo.register(ChatMessageSendC2S.class, CodecSerializer.INSTANCE);

        kryo.register(EntityRemoveS2C.class, CodecSerializer.INSTANCE);
        kryo.register(EntitySpawnS2C.class, CodecSerializer.INSTANCE);
        kryo.register(SpawnEntitiesS2C.class, CodecSerializer.INSTANCE);
        kryo.register(EntityDisplayS2C.class, CodecSerializer.INSTANCE);
        kryo.register(DisplayEntitiesS2C.class, CodecSerializer.INSTANCE);
        kryo.register(MoveEntitiesS2C.class, CodecSerializer.INSTANCE);
        kryo.register(MoveEntityS2C.class, CodecSerializer.INSTANCE);
        kryo.register(InventoryUpdateS2C.class, CodecSerializer.INSTANCE);
        kryo.register(LivingStatUpdateS2C.class, CodecSerializer.INSTANCE);
        kryo.register(LivingStatsUpdateS2C.class, CodecSerializer.INSTANCE);
        kryo.register(LootDropCloseS2C.class, CodecSerializer.INSTANCE);
        kryo.register(LootDropOpenS2C.class, CodecSerializer.INSTANCE);
        kryo.register(ChatMessageReceiveS2C.class, CodecSerializer.INSTANCE);
        //kryo.register(StatusEffectS2C.class, serializer);
        kryo.register(SyncDataS2C.class, new Serializer<SyncDataS2C>() {
            @Override
            public void write(Kryo kryo, Output output, SyncDataS2C msg) {
                output.writeInt(msg.data().length);
                output.writeBytes(msg.data());
            }

            @Override
            public SyncDataS2C read(Kryo kryo, Input input, Class<SyncDataS2C> aClass) {
                int len = input.readInt();
                byte[] bytes = input.readBytes(len);
                return new SyncDataS2C(bytes);
            }
        });

        kryo.register(InventoryType.class, CodecSerializer.INSTANCE);
        kryo.register(EntityGroup.class, CodecSerializer.INSTANCE);
        kryo.register(EntityCustomData.class, CodecSerializer.INSTANCE);
        kryo.register(StatContainer.class, CodecSerializer.INSTANCE);
        kryo.register(Slot.class, CodecSerializer.INSTANCE);
        kryo.register(Slot.Type.class, CodecSerializer.INSTANCE);
        kryo.register(Rarity.class, CodecSerializer.INSTANCE);
        kryo.register(ModifierEntry.class, CodecSerializer.INSTANCE);
        kryo.register(Item.class, CodecSerializer.INSTANCE);
        kryo.register(ItemStack.class, CodecSerializer.INSTANCE);
        kryo.register(Attack.class, CodecSerializer.INSTANCE);
        kryo.register(Action.class, CodecSerializer.INSTANCE);
        kryo.register(Attack.Type.class, CodecSerializer.INSTANCE);
        kryo.register(Action.Type.class, CodecSerializer.INSTANCE);
        kryo.register(Behavior.class, CodecSerializer.INSTANCE);
        kryo.register(Behavior.Type.class, CodecSerializer.INSTANCE);
        kryo.register(Phase.class, CodecSerializer.INSTANCE);
        kryo.register(BulletPath.class, CodecSerializer.INSTANCE);
        kryo.register(BulletPath.Type.class, CodecSerializer.INSTANCE);

        registerCodecSchema();
    }

    private static void registerCodecSchema() {
        CodecSerializer.registerSchema(CharacterJoinC2S.class, CharacterJoinC2S.CODEC);
        CodecSerializer.registerSchema(CharacterLeaveC2S.class, CharacterLeaveC2S.CODEC);
        CodecSerializer.registerSchema(CharacterMoveC2S.class, CharacterMoveC2S.CODEC);
        CodecSerializer.registerSchema(DropSlotItemC2S.class, DropSlotItemC2S.CODEC);
        CodecSerializer.registerSchema(ExecuteCommandC2S.class, ExecuteCommandC2S.CODEC);
        CodecSerializer.registerSchema(LootDropCloseC2S.class, LootDropCloseC2S.CODEC);
        CodecSerializer.registerSchema(SlotUpdateC2S.class, SlotUpdateC2S.CODEC);
        CodecSerializer.registerSchema(UseItemC2S.class, UseItemC2S.CODEC);
        CodecSerializer.registerSchema(AttackC2S.class, AttackC2S.CODEC);
        CodecSerializer.registerSchema(ChatMessageSendC2S.class, ChatMessageSendC2S.CODEC);
        CodecSerializer.registerSchema(EntityRemoveS2C.class, EntityRemoveS2C.CODEC);
        CodecSerializer.registerSchema(EntitySpawnS2C.class, EntitySpawnS2C.CODEC);
        CodecSerializer.registerSchema(SpawnEntitiesS2C.class, SpawnEntitiesS2C.CODEC);
        CodecSerializer.registerSchema(EntityDisplayS2C.class, EntityDisplayS2C.CODEC);
        CodecSerializer.registerSchema(DisplayEntitiesS2C.class, DisplayEntitiesS2C.CODEC);
        CodecSerializer.registerSchema(MoveEntitiesS2C.class, MoveEntitiesS2C.CODEC);
        CodecSerializer.registerSchema(MoveEntityS2C.class, MoveEntityS2C.CODEC);
        CodecSerializer.registerSchema(InventoryUpdateS2C.class, InventoryUpdateS2C.CODEC);
        CodecSerializer.registerSchema(LivingStatUpdateS2C.class, LivingStatUpdateS2C.CODEC);
        CodecSerializer.registerSchema(LivingStatsUpdateS2C.class, LivingStatsUpdateS2C.CODEC);
        CodecSerializer.registerSchema(LootDropCloseS2C.class, LootDropCloseS2C.CODEC);
        CodecSerializer.registerSchema(LootDropOpenS2C.class, LootDropOpenS2C.CODEC);
        CodecSerializer.registerSchema(ChatMessageReceiveS2C.class, ChatMessageReceiveS2C.CODEC);

        CodecSerializer.registerSchema(InventoryType.class, InventoryType.CODEC);
        CodecSerializer.registerSchema(EntityGroup.class, EntityGroup.CODEC);
        CodecSerializer.registerSchema(EntityCustomData.class, EntityCustomData.CODEC);
        CodecSerializer.registerSchema(StatContainer.class, StatContainer.STAT_CODEC);
        CodecSerializer.registerSchema(Slot.class, Slot.CODEC);
        CodecSerializer.registerSchema(Slot.Type.class, Slot.Type.CODEC);
        CodecSerializer.registerSchema(Rarity.class, Rarity.CODEC);
        CodecSerializer.registerSchema(Item.class, Item.CODEC);
        CodecSerializer.registerSchema(ItemStack.class, ItemStack.CODEC);
        CodecSerializer.registerSchema(Attack.class, Attack.CODEC);
        CodecSerializer.registerSchema(Action.class, Action.CODEC);
        CodecSerializer.registerSchema(Attack.Type.class, Attack.Type.CODEC);
        CodecSerializer.registerSchema(Action.Type.class, Action.Type.CODEC);
        CodecSerializer.registerSchema(Behavior.class, Behavior.CODEC);
        CodecSerializer.registerSchema(Behavior.Type.class, Behavior.Type.CODEC);
        CodecSerializer.registerSchema(Phase.class, Phase.CODEC);
        CodecSerializer.registerSchema(BulletPath.class, BulletPath.CODEC);
        CodecSerializer.registerSchema(BulletPath.Type.class, BulletPath.Type.CODEC);
        CodecSerializer.registerSchema(Message.class, Message.CODEC);
        CodecSerializer.registerSchema(Color.class, Codecs.COLOR);
        CodecSerializer.registerSchema(UUID.class, Codecs.UUID);
    }
}
