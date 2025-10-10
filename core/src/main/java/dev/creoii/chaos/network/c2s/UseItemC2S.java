package dev.creoii.chaos.network.c2s;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.inventory.Slot;

import java.io.Serializable;

public record UseItemC2S(int id, Slot slot) implements Serializable {
    public static final Codec<UseItemC2S> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(UseItemC2S::id),
            Slot.CODEC.fieldOf("slot").forGetter(UseItemC2S::slot)
        ).apply(instance, UseItemC2S::new);
    });
}
