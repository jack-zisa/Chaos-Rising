package dev.creoii.chaos.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import dev.creoii.chaos.inventory.InventoryType;
import dev.creoii.chaos.inventory.SlotEntry;
import dev.creoii.chaos.network.packet.c2s.*;
import dev.creoii.chaos.network.packet.s2c.*;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.stat.ModifierEntry;

/*
TODO: Custom serializers
 */
public class Networking {
    public static void register(Kryo kryo) {
        JavaSerializer serializer = new JavaSerializer();

        kryo.register(CharacterJoinC2S.class, new CodecSerializer<>(CharacterJoinC2S.CODEC));
        kryo.register(CharacterLeaveC2S.class, new CodecSerializer<>(CharacterLeaveC2S.CODEC));
        kryo.register(CharacterMoveC2S.class, new CodecSerializer<>(CharacterMoveC2S.CODEC));
        kryo.register(DropSlotItemC2S.class, serializer);
        kryo.register(ExecuteCommandC2S.class, new CodecSerializer<>(ExecuteCommandC2S.CODEC));
        kryo.register(LootDropCloseC2S.class, new CodecSerializer<>(LootDropCloseC2S.CODEC));
        kryo.register(SlotUpdateC2S.class, serializer);
        kryo.register(UseItemC2S.class, serializer);

        kryo.register(EntityRemoveS2C.class, new CodecSerializer<>(EntityRemoveS2C.CODEC));
        kryo.register(EntitySpawnS2C.class, new CodecSerializer<>(EntitySpawnS2C.CODEC));
        kryo.register(EntityDisplayS2C.class, new CodecSerializer<>(EntityDisplayS2C.CODEC));
        kryo.register(EntityMoveS2C.class, new CodecSerializer<>(EntityMoveS2C.CODEC));
        kryo.register(InventoryUpdateS2C.class, serializer);
        kryo.register(LootDropCloseS2C.class, new CodecSerializer<>(LootDropCloseS2C.CODEC));
        kryo.register(LootDropOpenS2C.class, new CodecSerializer<>(LootDropOpenS2C.CODEC));
        kryo.register(StatusEffectS2C.class, serializer);
        kryo.register(SyncDataS2C.class, new CodecSerializer<>(SyncDataS2C.CODEC));

        kryo.register(InventoryType.class, new CodecSerializer<>(InventoryType.CODEC));
        kryo.register(SlotEntry.class, serializer);
        kryo.register(EntityGroup.class, new CodecSerializer<>(EntityGroup.CODEC));
        kryo.register(ModifierEntry.class, serializer);
    }
}
