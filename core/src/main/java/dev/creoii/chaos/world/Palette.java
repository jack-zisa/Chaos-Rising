package dev.creoii.chaos.world;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.util.provider.tileprovider.TileProvider;

import java.util.Map;

public record Palette(Map<String, TileProvider> entries) {
    public static final Codec<Palette> CODEC = Codec.unboundedMap(Codec.STRING, TileProvider.CODEC).xmap(Palette::new, Palette::entries);
}
