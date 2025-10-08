package dev.creoii.chaos.network.packet.s2c;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.util.Codecs;

import java.io.Serializable;
import java.util.UUID;

public record LootDropOpenS2C(UUID uuid) implements Serializable {
    public static final Codec<LootDropOpenS2C> CODEC = Codecs.UUID.xmap(LootDropOpenS2C::new, LootDropOpenS2C::uuid);
}
