package dev.creoii.chaos.network;

import com.esotericsoftware.kryo.Kryo;
import dev.creoii.chaos.network.c2s.*;
import dev.creoii.chaos.network.s2c.*;
import dev.creoii.chaos.util.logging.Logger;

public class PacketRegistry {
    public static final Logger LOGGER = new Logger(PacketRegistry.class.getSimpleName());

    public static void register(Kryo kryo) {
        kryo.register(CharacterJoinC2S.class, PacketSerializer.INSTANCE);
        kryo.register(CharacterLeaveC2S.class, PacketSerializer.INSTANCE);
        kryo.register(CharacterMoveStartC2S.class, PacketSerializer.INSTANCE);
        kryo.register(CharacterMoveEndC2S.class, PacketSerializer.INSTANCE);
        kryo.register(CharacterStopMoveC2S.class, PacketSerializer.INSTANCE);
        kryo.register(DropSlotItemC2S.class, PacketSerializer.INSTANCE);
        kryo.register(ExecuteCommandC2S.class, PacketSerializer.INSTANCE);
        kryo.register(LootDropCloseC2S.class, PacketSerializer.INSTANCE);
        kryo.register(SlotUpdateC2S.class, PacketSerializer.INSTANCE);
        kryo.register(UseItemC2S.class, PacketSerializer.INSTANCE);
        kryo.register(AttackC2S.class, PacketSerializer.INSTANCE);
        kryo.register(ChatMessageSendC2S.class, PacketSerializer.INSTANCE);
        kryo.register(RequestWorldLoadC2S.class, PacketSerializer.INSTANCE);
        kryo.register(ClickSlotC2S.class, PacketSerializer.INSTANCE);

        kryo.register(EntityRemoveS2C.class, PacketSerializer.INSTANCE);
        kryo.register(RemoveEntitiesS2C.class, PacketSerializer.INSTANCE);
        kryo.register(EntitySpawnS2C.class, PacketSerializer.INSTANCE);
        kryo.register(CharacterJoinS2C.class, PacketSerializer.INSTANCE);
        kryo.register(SpawnEntitiesS2C.class, PacketSerializer.INSTANCE);
        kryo.register(MoveEntitiesS2C.class, PacketSerializer.INSTANCE);
        kryo.register(MoveEntityS2C.class, PacketSerializer.INSTANCE);
        kryo.register(EntityDisplayS2C.class, PacketSerializer.INSTANCE);
        kryo.register(EntityDamageS2C.class, PacketSerializer.INSTANCE);
        kryo.register(InventoryUpdateS2C.class, PacketSerializer.INSTANCE);
        kryo.register(SlotUpdateS2C.class, PacketSerializer.INSTANCE);
        kryo.register(LivingStatUpdateS2C.class, PacketSerializer.INSTANCE);
        kryo.register(LivingStatsUpdateS2C.class, PacketSerializer.INSTANCE);
        kryo.register(LootDropCloseS2C.class, PacketSerializer.INSTANCE);
        kryo.register(LootDropOpenS2C.class, PacketSerializer.INSTANCE);
        kryo.register(ChatMessageReceiveS2C.class, PacketSerializer.INSTANCE);
        kryo.register(StatusEffectS2C.class, PacketSerializer.INSTANCE);
        kryo.register(SyncDataS2C.class, PacketSerializer.INSTANCE);
        kryo.register(LoadDataS2C.class, PacketSerializer.INSTANCE);
        kryo.register(GainExperienceS2C.class, PacketSerializer.INSTANCE);
        kryo.register(SetTileS2C.class, PacketSerializer.INSTANCE);
        kryo.register(SetTilesS2C.class, PacketSerializer.INSTANCE);
        kryo.register(PlaceSetpieceS2C.class, PacketSerializer.INSTANCE);
        kryo.register(SetupWorldS2C.class, PacketSerializer.INSTANCE);
        kryo.register(SyncAttacksS2C.class, PacketSerializer.INSTANCE);

        registerCodecSchema();
    }

    private static void registerCodecSchema() {
        PacketSerializer.INSTANCE.register(CharacterJoinC2S.class, CharacterJoinC2S::write, CharacterJoinC2S::read);
        PacketSerializer.INSTANCE.register(CharacterLeaveC2S.class, CharacterLeaveC2S::write, CharacterLeaveC2S::read);
        PacketSerializer.INSTANCE.register(CharacterMoveStartC2S.class, CharacterMoveStartC2S::write, CharacterMoveStartC2S::read);
        PacketSerializer.INSTANCE.register(CharacterMoveEndC2S.class, CharacterMoveEndC2S::write, CharacterMoveEndC2S::read);
        PacketSerializer.INSTANCE.register(CharacterStopMoveC2S.class, CharacterStopMoveC2S::write, CharacterStopMoveC2S::read);
        PacketSerializer.INSTANCE.register(DropSlotItemC2S.class, DropSlotItemC2S::write, DropSlotItemC2S::read);
        PacketSerializer.INSTANCE.register(ExecuteCommandC2S.class, ExecuteCommandC2S::write, ExecuteCommandC2S::read);
        PacketSerializer.INSTANCE.register(LootDropCloseC2S.class, LootDropCloseC2S::write, LootDropCloseC2S::read);
        PacketSerializer.INSTANCE.register(SlotUpdateC2S.class, SlotUpdateC2S::write, SlotUpdateC2S::read);
        PacketSerializer.INSTANCE.register(UseItemC2S.class, UseItemC2S::write, UseItemC2S::read);
        PacketSerializer.INSTANCE.register(AttackC2S.class, AttackC2S::write, AttackC2S::read);
        PacketSerializer.INSTANCE.register(ChatMessageSendC2S.class, ChatMessageSendC2S::write, ChatMessageSendC2S::read);
        PacketSerializer.INSTANCE.register(RequestWorldLoadC2S.class, RequestWorldLoadC2S::write, RequestWorldLoadC2S::read);
        PacketSerializer.INSTANCE.register(ClickSlotC2S.class, ClickSlotC2S::write, ClickSlotC2S::read);

        PacketSerializer.INSTANCE.register(EntityRemoveS2C.class, EntityRemoveS2C::write, EntityRemoveS2C::read);
        PacketSerializer.INSTANCE.register(RemoveEntitiesS2C.class, RemoveEntitiesS2C::write, RemoveEntitiesS2C::read);
        PacketSerializer.INSTANCE.register(EntitySpawnS2C.class, EntitySpawnS2C::write, EntitySpawnS2C::read);
        PacketSerializer.INSTANCE.register(CharacterJoinS2C.class, CharacterJoinS2C::write, CharacterJoinS2C::read);
        PacketSerializer.INSTANCE.register(SpawnEntitiesS2C.class, SpawnEntitiesS2C::write, SpawnEntitiesS2C::read);
        PacketSerializer.INSTANCE.register(MoveEntitiesS2C.class, MoveEntitiesS2C::write, MoveEntitiesS2C::read);
        PacketSerializer.INSTANCE.register(MoveEntityS2C.class, MoveEntityS2C::write, MoveEntityS2C::read);
        PacketSerializer.INSTANCE.register(EntityDisplayS2C.class, EntityDisplayS2C::write, EntityDisplayS2C::read);
        PacketSerializer.INSTANCE.register(EntityDamageS2C.class, EntityDamageS2C::write, EntityDamageS2C::read);
        PacketSerializer.INSTANCE.register(InventoryUpdateS2C.class, InventoryUpdateS2C::write, InventoryUpdateS2C::read);
        PacketSerializer.INSTANCE.register(SlotUpdateS2C.class, SlotUpdateS2C::write, SlotUpdateS2C::read);
        PacketSerializer.INSTANCE.register(LivingStatUpdateS2C.class, LivingStatUpdateS2C::write, LivingStatUpdateS2C::read);
        PacketSerializer.INSTANCE.register(LivingStatsUpdateS2C.class, LivingStatsUpdateS2C::write, LivingStatsUpdateS2C::read);
        PacketSerializer.INSTANCE.register(LootDropCloseS2C.class, LootDropCloseS2C::write, LootDropCloseS2C::read);
        PacketSerializer.INSTANCE.register(LootDropOpenS2C.class, LootDropOpenS2C::write, LootDropOpenS2C::read);
        PacketSerializer.INSTANCE.register(ChatMessageReceiveS2C.class, ChatMessageReceiveS2C::write, ChatMessageReceiveS2C::read);
        PacketSerializer.INSTANCE.register(StatusEffectS2C.class, StatusEffectS2C::write, StatusEffectS2C::read);
        PacketSerializer.INSTANCE.register(SyncDataS2C.class, SyncDataS2C::write, SyncDataS2C::read);
        PacketSerializer.INSTANCE.register(LoadDataS2C.class, LoadDataS2C::write, LoadDataS2C::read);
        PacketSerializer.INSTANCE.register(GainExperienceS2C.class, GainExperienceS2C::write, GainExperienceS2C::read);
        PacketSerializer.INSTANCE.register(SetTileS2C.class, SetTileS2C::write, SetTileS2C::read);
        PacketSerializer.INSTANCE.register(SetTilesS2C.class, SetTilesS2C::write, SetTilesS2C::read);
        PacketSerializer.INSTANCE.register(PlaceSetpieceS2C.class, PlaceSetpieceS2C::write, PlaceSetpieceS2C::read);
        PacketSerializer.INSTANCE.register(SetupWorldS2C.class, SetupWorldS2C::write, SetupWorldS2C::read);
        PacketSerializer.INSTANCE.register(SyncAttacksS2C.class, SyncAttacksS2C::write, SyncAttacksS2C::read);
    }
}
