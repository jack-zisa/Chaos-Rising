package dev.creoii.chaos.world.tile;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.util.Identifiable;
import dev.creoii.chaos.util.Light;

public record Tile(String id, String texture, Light light) implements Identifiable {
    public static final Codec<Tile> VALUE_CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("id").forGetter(Tile::id),
            Codec.STRING.fieldOf("texture").forGetter(Tile::texture),
            Light.CODEC.fieldOf("light").orElse(Light.EMPTY).forGetter(Tile::light)
        ).apply(instance, Tile::new);
    });
    public static final Codec<Tile> ID_CODEC = Codec.STRING.xmap(DataManager::getTile, Tile::id);

    public boolean hasLight() {
        return light != Light.EMPTY;
    }

    public boolean isSolid() {
        return "stone".equals(id);
    }
}
