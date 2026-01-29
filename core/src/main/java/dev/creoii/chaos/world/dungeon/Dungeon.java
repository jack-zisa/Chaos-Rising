package dev.creoii.chaos.world.dungeon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.util.Identifiable;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;
import dev.creoii.chaos.util.provider.roomprovider.RoomProvider;

public record Dungeon(String id, NumberProvider maxDepth, RoomProvider fallback) implements Identifiable {
    public static final Codec<Dungeon> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("id").forGetter(Dungeon::id),
            NumberProvider.CODEC.fieldOf("max_depth").forGetter(Dungeon::maxDepth),
            RoomProvider.CODEC.fieldOf("fallback").forGetter(Dungeon::fallback)
        ).apply(instance, Dungeon::new);
    });
    public static final Codec<Dungeon> ID_CODEC = Codec.STRING.xmap(DataManager::getDungeon, Dungeon::id);
}
