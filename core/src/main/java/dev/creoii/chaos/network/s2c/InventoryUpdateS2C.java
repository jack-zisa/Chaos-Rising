package dev.creoii.chaos.network.s2c;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.inventory.InventoryType;
import dev.creoii.chaos.inventory.Slot;

import java.io.Serializable;
import java.util.List;

public record InventoryUpdateS2C(InventoryType type, List<Slot> slots) implements Serializable {
    public static final Codec<InventoryUpdateS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            InventoryType.CODEC.fieldOf("type").forGetter(InventoryUpdateS2C::type),
            Slot.CODEC.listOf().fieldOf("slots").forGetter(InventoryUpdateS2C::slots)
        ).apply(instance, InventoryUpdateS2C::new);
    });
}
