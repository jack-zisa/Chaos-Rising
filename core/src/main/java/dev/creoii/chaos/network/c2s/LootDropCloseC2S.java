package dev.creoii.chaos.network.c2s;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.util.Codecs;

import java.io.Serializable;
import java.util.UUID;

public record LootDropCloseC2S(UUID uuid) implements Serializable {
    public static final Codec<LootDropCloseC2S> CODEC = Codecs.UUID.xmap(LootDropCloseC2S::new, LootDropCloseC2S::uuid);
}
