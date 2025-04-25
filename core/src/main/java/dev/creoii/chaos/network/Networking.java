package dev.creoii.chaos.network;

import com.esotericsoftware.kryo.Kryo;
import dev.creoii.chaos.network.packet.c2s.CharacterJoinC2S;
import dev.creoii.chaos.network.packet.c2s.ExecuteCommandC2S;
import dev.creoii.chaos.network.packet.c2s.KeyInputC2S;
import dev.creoii.chaos.network.packet.c2s.MouseInputC2S;
import dev.creoii.chaos.network.packet.s2c.EntityDeathS2C;
import dev.creoii.chaos.network.packet.s2c.EntitySpawnS2C;
import dev.creoii.chaos.network.packet.s2c.EntityStateS2C;

public class Networking {
    public static void register(Kryo kryo) {
        kryo.register(CharacterJoinC2S.class);
        kryo.register(ExecuteCommandC2S.class);
        kryo.register(KeyInputC2S.class);
        kryo.register(MouseInputC2S.class);

        kryo.register(EntityDeathS2C.class);
        kryo.register(EntitySpawnS2C.class);
        kryo.register(EntityStateS2C.class);
    }
}
