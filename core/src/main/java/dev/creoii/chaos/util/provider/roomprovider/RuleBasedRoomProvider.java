package dev.creoii.chaos.util.provider.roomprovider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.util.provider.booleanprovider.BooleanProvider;
import dev.creoii.chaos.world.dungeon.room.RoomTemplate;

import javax.annotation.Nullable;
import java.util.List;

public record RuleBasedRoomProvider(List<Entry> entries) implements RoomProvider {
    public static final MapCodec<RuleBasedRoomProvider> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Entry.CODEC.listOf().fieldOf("entries").forGetter(RuleBasedRoomProvider::entries)
        ).apply(instance, RuleBasedRoomProvider::new)
    );

    @Override
    public Type getType() {
        return Type.SIMPLE;
    }

    @Override
    @Nullable
    public RoomTemplate get(ContextProvider context) {
        for (Entry entry : entries) {
            if (entry.test.get(context))
                return entry.roomProvider.get(context);
        }
        return null;
    }

    public record Entry(BooleanProvider test, RoomProvider roomProvider) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
                BooleanProvider.DISPATCH_CODEC.fieldOf("test").forGetter(Entry::test),
                RoomProvider.CODEC.fieldOf("room").forGetter(Entry::roomProvider)
            ).apply(instance, Entry::new);
        });
    }
}
