package dev.creoii.chaos.world.map;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.World;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.tileprovider.SimpleTileProvider;
import dev.creoii.chaos.util.provider.tileprovider.TileProvider;
import dev.creoii.chaos.world.Palette;
import dev.creoii.chaos.world.noise.FastNoiseLite;
import dev.creoii.chaos.world.noise.FastNoiseParameters;

import java.util.Comparator;
import java.util.List;

public record NoiseBasedWorldMap(String id, int width, int height, Palette palette, FastNoiseParameters noise, List<Entry> entries) implements WorldMap {
    public static final MapCodec<NoiseBasedWorldMap> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.STRING.fieldOf("id").forGetter(NoiseBasedWorldMap::id),
        Codec.INT.fieldOf("width").forGetter(NoiseBasedWorldMap::width),
        Codec.INT.fieldOf("height").forGetter(NoiseBasedWorldMap::height),
        Palette.CODEC.fieldOf("palette").forGetter(NoiseBasedWorldMap::palette),
        FastNoiseParameters.CODEC.fieldOf("noise").forGetter(NoiseBasedWorldMap::noise),
        Entry.CODEC.listOf().fieldOf("entries").forGetter(NoiseBasedWorldMap::entries)
    ).apply(instance, NoiseBasedWorldMap::new));

    @Override
    public Type getType() {
        return Type.NOISE_BASED;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void place(World world) {
        if (entries.isEmpty())
            return;

        FastNoiseLite fastNoiseLite = new FastNoiseLite(noise).seed(world.getSeed());
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double n = fastNoiseLite.getNoise(x, y);

                for (Entry entry : entries) {
                    if (n <= entry.max()) {
                        TileProvider tileProvider = palette.entries().getOrDefault(entry.tile, SimpleTileProvider.EMPTY);
                        if (tileProvider != SimpleTileProvider.EMPTY) {
                            String tile = tileProvider.get(new Provider.Context(world.getGame(), world, null, world.getGame().getGametime(), new Vector2(x, y), world.getRandom()));
                            System.out.println(n + ": " + entry.toString() + ": " + tile);
                            world.setGround(x, y, tile);
                            break;
                        }
                    }
                }
            }
        }
    }

    record Entry(float max, String tile) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("max").forGetter(Entry::max),
            Codec.STRING.fieldOf("tile").forGetter(Entry::tile)
        ).apply(instance, Entry::new));
    }
}
