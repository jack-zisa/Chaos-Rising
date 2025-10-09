package dev.creoii.chaos.inventory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.item.ItemStack;

import java.io.Serializable;

public record SlotEntry(int r, int c, Slot.Type type, ItemStack stack, boolean active) implements Serializable {
    public static final Codec<SlotEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("r").forGetter(SlotEntry::r),
        Codec.INT.fieldOf("c").forGetter(SlotEntry::c),
        Slot.Type.CODEC.fieldOf("type").forGetter(SlotEntry::type),
        ItemStack.CODEC.fieldOf("stack").forGetter(SlotEntry::stack),
        Codec.BOOL.fieldOf("active").forGetter(SlotEntry::active)
    ).apply(instance, SlotEntry::new));
}
