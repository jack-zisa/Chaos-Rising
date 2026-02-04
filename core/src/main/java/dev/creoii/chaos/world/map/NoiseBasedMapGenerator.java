package dev.creoii.chaos.world.map;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.World;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.Context;
import dev.creoii.chaos.util.provider.tileprovider.SimpleTileProvider;
import dev.creoii.chaos.util.provider.tileprovider.TileProvider;
import dev.creoii.chaos.world.noise.FastNoiseLite;
import dev.creoii.chaos.world.noise.FastNoiseParameters;
import dev.creoii.chaos.world.tile.Tile;

import java.util.List;

public record NoiseBasedMapGenerator(String id, int width, int height, FastNoiseParameters noise, List<Entry> entries, float ambientLight) implements MapGenerator {
    public static final MapCodec<NoiseBasedMapGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.STRING.fieldOf("id").forGetter(NoiseBasedMapGenerator::id),
        Codec.INT.fieldOf("width").forGetter(NoiseBasedMapGenerator::width),
        Codec.INT.fieldOf("height").forGetter(NoiseBasedMapGenerator::height),
        FastNoiseParameters.CODEC.fieldOf("noise").forGetter(NoiseBasedMapGenerator::noise),
        Entry.CODEC.listOf().fieldOf("entries").forGetter(NoiseBasedMapGenerator::entries),
        Codec.FLOAT.fieldOf("ambient_light").orElse(1f).forGetter(NoiseBasedMapGenerator::ambientLight)
    ).apply(instance, NoiseBasedMapGenerator::new));

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

    @Override
    public void build(World world) {

    }

    public void place(World world) {
        if (entries.isEmpty())
            return;

        Context context = Context.rootOf(world);
        FastNoiseLite fastNoiseLite = new FastNoiseLite(noise).seed(world.getSeed());
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double n = fastNoiseLite.getNoise(x, y);

                for (Entry entry : entries) {
                    if (n <= entry.max()) {
                        TileProvider tileProvider = entry.tile;
                        if (tileProvider != SimpleTileProvider.EMPTY) {
                            Tile tile = tileProvider.get(context.child().with(ComponentTypes.POS, new Vector2(x, y)));
                            world.setGround(x, y, tile);
                            break;
                        }
                    }
                }
            }
        }
    }

    record Entry(float max, TileProvider tile) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("max").forGetter(Entry::max),
            TileProvider.CODEC.fieldOf("tile").forGetter(Entry::tile)
        ).apply(instance, Entry::new));
    }

    @Override
    public Vector2 getSpawnPos() {
        return Vector2.Zero;
    }

    @Override
    public float getAmbientLight() {
        return ambientLight;
    }
}
