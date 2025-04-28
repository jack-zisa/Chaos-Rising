package dev.creoii.chaos.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import dev.creoii.chaos.inventory.InventoryType;
import dev.creoii.chaos.inventory.SlotEntry;
import dev.creoii.chaos.network.packet.c2s.*;
import dev.creoii.chaos.network.packet.s2c.*;

public class Networking {
    public static void register(Kryo kryo) {
        JavaSerializer serializer = new JavaSerializer();

        kryo.register(CharacterJoinC2S.class, serializer);
        kryo.register(CharacterLeaveC2S.class, serializer);
        kryo.register(CharacterMoveC2S.class, serializer);
        kryo.register(ExecuteCommandC2S.class, serializer);
        kryo.register(LootDropCloseC2S.class, serializer);
        kryo.register(SlotUpdateC2S.class, serializer);

        kryo.register(CharacterSpawnS2C.class, serializer);
        kryo.register(EntityRemoveS2C.class, serializer);
        kryo.register(EntitySpawnS2C.class, serializer);
        kryo.register(EntityStateS2C.class, serializer);
        kryo.register(InventoryUpdateS2C.class, serializer);
        kryo.register(LivingEntityStateS2C.class, serializer);
        kryo.register(LootDropCloseS2C.class, serializer);
        kryo.register(LootDropOpenS2C.class, serializer);
        kryo.register(StatusEffectS2C.class, serializer);
        kryo.register(SyncDataS2C.class, serializer);

        kryo.register(InventoryType.class, serializer);
        kryo.register(SlotEntry.class, serializer);
    }
}
