package dev.creoii.chaos.network;

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.kryo.Kryo;
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
        kryo.register(CharacterJoinC2S.class, PacketSerializer.INSTANCE);
        kryo.register(CharacterLeaveC2S.class, PacketSerializer.INSTANCE);
        kryo.register(CharacterMoveC2S.class, PacketSerializer.INSTANCE);
        kryo.register(DropSlotItemC2S.class, PacketSerializer.INSTANCE);
        kryo.register(ExecuteCommandC2S.class, PacketSerializer.INSTANCE);
        kryo.register(LootDropCloseC2S.class, PacketSerializer.INSTANCE);
        kryo.register(SlotUpdateC2S.class, PacketSerializer.INSTANCE);
        kryo.register(UseItemC2S.class, PacketSerializer.INSTANCE);
        kryo.register(AttackC2S.class, PacketSerializer.INSTANCE);
        kryo.register(ChatMessageSendC2S.class, PacketSerializer.INSTANCE);

        kryo.register(EntityRemoveS2C.class, PacketSerializer.INSTANCE);
        kryo.register(EntitySpawnS2C.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(SpawnEntitiesS2C.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(EntityDisplayS2C.class, PacketSerializer.INSTANCE);
        kryo.register(DisplayEntitiesS2C.class, PacketSerializer.INSTANCE);
        kryo.register(MoveEntitiesS2C.class, PacketSerializer.INSTANCE);
        kryo.register(MoveEntityS2C.class, PacketSerializer.INSTANCE);
        kryo.register(InventoryUpdateS2C.class, PacketSerializer.INSTANCE);
        kryo.register(LivingStatUpdateS2C.class, PacketSerializer.INSTANCE);
        kryo.register(LivingStatsUpdateS2C.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(LootDropCloseS2C.class, PacketSerializer.INSTANCE);
        kryo.register(LootDropOpenS2C.class, PacketSerializer.INSTANCE);
        kryo.register(ChatMessageReceiveS2C.class, PacketSerializer.INSTANCE);
        //kryo.register(StatusEffectS2C.class, serializer);
        kryo.register(SyncDataS2C.class, PacketSerializer.INSTANCE);

        kryo.register(InventoryType.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(EntityGroup.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(EntityCustomData.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(StatContainer.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(Slot.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(Slot.Type.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(Rarity.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(ModifierEntry.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(Item.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(ItemStack.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(Attack.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(Action.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(Attack.Type.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(Action.Type.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(Behavior.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(Behavior.Type.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(Phase.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(BulletPath.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(BulletPath.Type.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(Color.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(UUID.class, LegacyCodecSerializer.INSTANCE);
        kryo.register(Message.class, LegacyCodecSerializer.INSTANCE);

        registerCodecSchema();
    }

    private static void registerCodecSchema() {
        PacketSerializer.INSTANCE.register(CharacterJoinC2S.class, CharacterJoinC2S::write, CharacterJoinC2S::read);
        PacketSerializer.INSTANCE.register(CharacterLeaveC2S.class, CharacterLeaveC2S::write, CharacterLeaveC2S::read);
        PacketSerializer.INSTANCE.register(CharacterMoveC2S.class, CharacterMoveC2S::write, CharacterMoveC2S::read);
        PacketSerializer.INSTANCE.register(DropSlotItemC2S.class, DropSlotItemC2S::write, DropSlotItemC2S::read);
        PacketSerializer.INSTANCE.register(ExecuteCommandC2S.class, ExecuteCommandC2S::write, ExecuteCommandC2S::read);
        PacketSerializer.INSTANCE.register(LootDropCloseC2S.class, LootDropCloseC2S::write, LootDropCloseC2S::read);
        PacketSerializer.INSTANCE.register(SlotUpdateC2S.class, SlotUpdateC2S::write, SlotUpdateC2S::read);
        PacketSerializer.INSTANCE.register(UseItemC2S.class, UseItemC2S::write, UseItemC2S::read);
        PacketSerializer.INSTANCE.register(AttackC2S.class, AttackC2S::write, AttackC2S::read);
        PacketSerializer.INSTANCE.register(ChatMessageSendC2S.class, ChatMessageSendC2S::write, ChatMessageSendC2S::read);

        PacketSerializer.INSTANCE.register(EntityRemoveS2C.class, EntityRemoveS2C::write, EntityRemoveS2C::read);
        LegacyCodecSerializer.registerSchema(EntitySpawnS2C.class, EntitySpawnS2C.CODEC);
        LegacyCodecSerializer.registerSchema(SpawnEntitiesS2C.class, SpawnEntitiesS2C.CODEC);
        PacketSerializer.INSTANCE.register(EntityDisplayS2C.class, EntityDisplayS2C::write, EntityDisplayS2C::read);
        PacketSerializer.INSTANCE.register(DisplayEntitiesS2C.class, DisplayEntitiesS2C::write, DisplayEntitiesS2C::read);
        PacketSerializer.INSTANCE.register(MoveEntitiesS2C.class, MoveEntitiesS2C::write, MoveEntitiesS2C::read);
        PacketSerializer.INSTANCE.register(MoveEntityS2C.class, MoveEntityS2C::write, MoveEntityS2C::read);
        PacketSerializer.INSTANCE.register(InventoryUpdateS2C.class, InventoryUpdateS2C::write, InventoryUpdateS2C::read);
        PacketSerializer.INSTANCE.register(LivingStatUpdateS2C.class, LivingStatUpdateS2C::write, LivingStatUpdateS2C::read);
        LegacyCodecSerializer.registerSchema(LivingStatsUpdateS2C.class, LivingStatsUpdateS2C.CODEC);
        PacketSerializer.INSTANCE.register(LootDropCloseS2C.class, LootDropCloseS2C::write, LootDropCloseS2C::read);
        PacketSerializer.INSTANCE.register(LootDropOpenS2C.class, LootDropOpenS2C::write, LootDropOpenS2C::read);
        PacketSerializer.INSTANCE.register(ChatMessageReceiveS2C.class, ChatMessageReceiveS2C::write, ChatMessageReceiveS2C::read);
        PacketSerializer.INSTANCE.register(SyncDataS2C.class, SyncDataS2C::write, SyncDataS2C::read);

        LegacyCodecSerializer.registerSchema(InventoryType.class, InventoryType.CODEC);
        LegacyCodecSerializer.registerSchema(EntityGroup.class, EntityGroup.CODEC);
        LegacyCodecSerializer.registerSchema(EntityCustomData.class, EntityCustomData.CODEC);
        LegacyCodecSerializer.registerSchema(StatContainer.class, StatContainer.STAT_CODEC);
        LegacyCodecSerializer.registerSchema(Slot.class, Slot.CODEC);
        LegacyCodecSerializer.registerSchema(Slot.Type.class, Slot.Type.CODEC);
        LegacyCodecSerializer.registerSchema(Rarity.class, Rarity.CODEC);
        LegacyCodecSerializer.registerSchema(ModifierEntry.class, ModifierEntry.CODEC);
        LegacyCodecSerializer.registerSchema(Item.class, Item.CODEC);
        LegacyCodecSerializer.registerSchema(ItemStack.class, ItemStack.CODEC);
        LegacyCodecSerializer.registerSchema(Attack.class, Attack.CODEC);
        LegacyCodecSerializer.registerSchema(Action.class, Action.CODEC);
        LegacyCodecSerializer.registerSchema(Attack.Type.class, Attack.Type.CODEC);
        LegacyCodecSerializer.registerSchema(Action.Type.class, Action.Type.CODEC);
        LegacyCodecSerializer.registerSchema(Behavior.class, Behavior.CODEC);
        LegacyCodecSerializer.registerSchema(Behavior.Type.class, Behavior.Type.CODEC);
        LegacyCodecSerializer.registerSchema(Phase.class, Phase.CODEC);
        LegacyCodecSerializer.registerSchema(BulletPath.class, BulletPath.CODEC);
        LegacyCodecSerializer.registerSchema(BulletPath.Type.class, BulletPath.Type.CODEC);
        LegacyCodecSerializer.registerSchema(Message.class, Message.CODEC);
        LegacyCodecSerializer.registerSchema(Color.class, Codecs.COLOR);
        LegacyCodecSerializer.registerSchema(UUID.class, Codecs.UUID);
    }
}
