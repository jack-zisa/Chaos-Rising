package dev.creoii.chaos.network;

import com.esotericsoftware.kryo.Kryo;
import dev.creoii.chaos.network.packet.c2s.*;
import dev.creoii.chaos.network.packet.s2c.*;

public class Networking {
    public static void register(Kryo kryo) {
        kryo.register(CharacterJoinC2S.class);
        kryo.register(CharacterLeaveC2S.class);
        kryo.register(ExecuteCommandC2S.class);
        kryo.register(KeyInputC2S.class);
        kryo.register(LootDropCloseC2S.class);
        kryo.register(MouseInputC2S.class);
        kryo.register(SlotUpdateC2S.class);

        kryo.register(CharacterSpawnS2C.class);
        kryo.register(EntityRemoveS2C.class);
        kryo.register(EntitySpawnS2C.class);
        kryo.register(EntityStateS2C.class);
        kryo.register(LivingEntityStateS2C.class);
        kryo.register(LootDropCloseS2C.class);
        kryo.register(LootDropOpenS2C.class);
        kryo.register(StatusEffectS2C.class);
    }
}
