package dev.creoii.chaos.network.packet.c2s;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.serialization.EntityCustomData;
import dev.creoii.chaos.inventory.SlotEntry;
import dev.creoii.chaos.network.packet.s2c.EntitySpawnS2C;
import dev.creoii.chaos.util.Codecs;

import java.io.Serializable;
import java.util.UUID;

public record UseItemC2S(UUID uuid, SlotEntry slot) implements Serializable {
    public static final Codec<UseItemC2S> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codecs.UUID.fieldOf("uuid").forGetter(UseItemC2S::uuid),
            SlotEntry.CODEC.fieldOf("slot").forGetter(UseItemC2S::slot)
        ).apply(instance, UseItemC2S::new);
    });
}
