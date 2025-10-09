package dev.creoii.chaos.network.packet.c2s;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.util.Codecs;

import java.io.Serializable;
import java.util.UUID;

public record UseItemC2S(UUID uuid, Slot slot) implements Serializable {
    public static final Codec<UseItemC2S> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codecs.UUID.fieldOf("uuid").forGetter(UseItemC2S::uuid),
            Slot.CODEC.fieldOf("slot").forGetter(UseItemC2S::slot)
        ).apply(instance, UseItemC2S::new);
    });
}
