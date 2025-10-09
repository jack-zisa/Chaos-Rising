package dev.creoii.chaos.network.packet.c2s;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.inventory.SlotEntry;
import dev.creoii.chaos.util.Codecs;

import java.io.Serializable;
import java.util.UUID;

public record DropSlotItemC2S(UUID uuid, SlotEntry slot) implements Serializable {
    public static final Codec<DropSlotItemC2S> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codecs.UUID.fieldOf("uuid").forGetter(DropSlotItemC2S::uuid),
            SlotEntry.CODEC.fieldOf("slot").forGetter(DropSlotItemC2S::slot)
        ).apply(instance, DropSlotItemC2S::new);
    });
}
