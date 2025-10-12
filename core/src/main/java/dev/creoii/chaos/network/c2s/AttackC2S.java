package dev.creoii.chaos.network.c2s;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.inventory.Slot;

import java.io.Serializable;

public record AttackC2S(int id, Slot slot, float mouseX, float mouseY) implements Serializable {
    public static final Codec<AttackC2S> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(AttackC2S::id),
            Slot.CODEC.fieldOf("slot").forGetter(AttackC2S::slot),
            Codec.FLOAT.fieldOf("mouse_x").forGetter(AttackC2S::mouseX),
            Codec.FLOAT.fieldOf("mouse_y").forGetter(AttackC2S::mouseY)
        ).apply(instance, AttackC2S::new);
    });
}
