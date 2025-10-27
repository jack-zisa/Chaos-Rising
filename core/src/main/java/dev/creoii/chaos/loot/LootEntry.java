package dev.creoii.chaos.loot;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.Game;
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
        ).apply(instance, LootEntry::new);
    });

    public ItemStack roll(Game game) {
        Item item = DataManager.getItem(item());
        if (item == null)
            return ItemStack.EMPTY;
        return new ItemStack(item, count.getInt(new Provider.Context(game, null, game.getGametime(), Vector2.Zero, game.getRandom())));
    }
}
