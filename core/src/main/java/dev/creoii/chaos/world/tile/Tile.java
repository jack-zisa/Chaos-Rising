package dev.creoii.chaos.world.tile;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.util.Identifiable;

public record Tile(String id, String texture) implements Identifiable {
    public static final Codec<Tile> VALUE_CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("id").forGetter(Tile::id),
            Codec.STRING.fieldOf("texture").forGetter(Tile::texture)
        ).apply(instance, Tile::new);
    });
    public static final Codec<Tile> ID_CODEC = Codec.STRING.xmap(DataManager::getTile, Tile::id);
}
