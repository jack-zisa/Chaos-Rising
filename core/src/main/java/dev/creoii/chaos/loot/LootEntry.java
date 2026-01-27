package dev.creoii.chaos.loot;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.World;
import dev.creoii.chaos.item.Item;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.numberprovider.ConstantNumberProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

public record LootEntry(String item, int weight, NumberProvider count) {
    public static final Codec<LootEntry> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("item").forGetter(LootEntry::item),
            Codec.INT.fieldOf("weight").orElse(1).forGetter(LootEntry::weight),
            NumberProvider.CODEC.fieldOf("count").orElse(ConstantNumberProvider.ONE).forGetter(LootEntry::count)
        ).apply(instance, (item, weight, count) -> new LootEntry(item, weight, (NumberProvider) count.optimize()));
    });

    public ItemStack roll(World world) {
        Item item = DataManager.getItem(item());
        if (item == null)
            return ItemStack.EMPTY;
        return new ItemStack(item, count.getInt(new Provider.Context(world.getGame(), world, null, world.getGame().getGametime(), Vector2.Zero, world.getRandom())));
    }
}
