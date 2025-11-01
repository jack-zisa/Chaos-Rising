package dev.creoii.chaos.network;

import com.esotericsoftware.kryo.Kryo;
import dev.creoii.chaos.network.c2s.*;
import dev.creoii.chaos.network.s2c.*;
import dev.creoii.chaos.util.logging.Logger;

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
        kryo.register(RemoveEntitiesS2C.class, PacketSerializer.INSTANCE);
        kryo.register(EntitySpawnS2C.class, PacketSerializer.INSTANCE);
        kryo.register(CharacterJoinS2C.class, PacketSerializer.INSTANCE);
        kryo.register(SpawnEntitiesS2C.class, PacketSerializer.INSTANCE);
        kryo.register(MoveEntitiesS2C.class, PacketSerializer.INSTANCE);
        kryo.register(MoveEntityS2C.class, PacketSerializer.INSTANCE);
        kryo.register(EntityDamageS2C.class, PacketSerializer.INSTANCE);
        kryo.register(InventoryUpdateS2C.class, PacketSerializer.INSTANCE);
        kryo.register(LivingStatUpdateS2C.class, PacketSerializer.INSTANCE);
        kryo.register(LivingStatsUpdateS2C.class, PacketSerializer.INSTANCE);
        kryo.register(LootDropCloseS2C.class, PacketSerializer.INSTANCE);
        kryo.register(LootDropOpenS2C.class, PacketSerializer.INSTANCE);
        kryo.register(ChatMessageReceiveS2C.class, PacketSerializer.INSTANCE);
        kryo.register(StatusEffectS2C.class, PacketSerializer.INSTANCE);
        kryo.register(SyncDataS2C.class, PacketSerializer.INSTANCE);

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
        PacketSerializer.INSTANCE.register(RemoveEntitiesS2C.class, RemoveEntitiesS2C::write, RemoveEntitiesS2C::read);
        PacketSerializer.INSTANCE.register(EntitySpawnS2C.class, EntitySpawnS2C::write, EntitySpawnS2C::read);
        PacketSerializer.INSTANCE.register(CharacterJoinS2C.class, CharacterJoinS2C::write, CharacterJoinS2C::read);
        PacketSerializer.INSTANCE.register(SpawnEntitiesS2C.class, SpawnEntitiesS2C::write, SpawnEntitiesS2C::read);
        PacketSerializer.INSTANCE.register(MoveEntitiesS2C.class, MoveEntitiesS2C::write, MoveEntitiesS2C::read);
        PacketSerializer.INSTANCE.register(MoveEntityS2C.class, MoveEntityS2C::write, MoveEntityS2C::read);
        PacketSerializer.INSTANCE.register(EntityDamageS2C.class, EntityDamageS2C::write, EntityDamageS2C::read);
        PacketSerializer.INSTANCE.register(InventoryUpdateS2C.class, InventoryUpdateS2C::write, InventoryUpdateS2C::read);
        PacketSerializer.INSTANCE.register(LivingStatUpdateS2C.class, LivingStatUpdateS2C::write, LivingStatUpdateS2C::read);
        PacketSerializer.INSTANCE.register(LivingStatsUpdateS2C.class, LivingStatsUpdateS2C::write, LivingStatsUpdateS2C::read);
        PacketSerializer.INSTANCE.register(LootDropCloseS2C.class, LootDropCloseS2C::write, LootDropCloseS2C::read);
        PacketSerializer.INSTANCE.register(LootDropOpenS2C.class, LootDropOpenS2C::write, LootDropOpenS2C::read);
        PacketSerializer.INSTANCE.register(ChatMessageReceiveS2C.class, ChatMessageReceiveS2C::write, ChatMessageReceiveS2C::read);
        PacketSerializer.INSTANCE.register(StatusEffectS2C.class, StatusEffectS2C::write, StatusEffectS2C::read);
        PacketSerializer.INSTANCE.register(SyncDataS2C.class, SyncDataS2C::write, SyncDataS2C::read);
    }
}
