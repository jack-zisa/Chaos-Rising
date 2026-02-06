package dev.creoii.chaos.util.provider.tileprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.World;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.world.noise.FastNoiseLite;
import dev.creoii.chaos.world.noise.FastNoiseParameters;
import dev.creoii.chaos.world.tile.Tile;

import java.util.List;

public record NoiseTileProvider(FastNoiseParameters noise, List<Entry> entries) implements TileProvider {
    public static final MapCodec<NoiseTileProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        FastNoiseParameters.CODEC.fieldOf("noise").forGetter(NoiseTileProvider::noise),
        Entry.CODEC.listOf().fieldOf("entries").forGetter(NoiseTileProvider::entries)
    ).apply(instance, NoiseTileProvider::new));

    @Override
    public Type getType() {
        return Type.NOISE;
    }

    @Override
    public Tile get(ContextProvider context) {
        if (entries.isEmpty())
            return SimpleTileProvider.EMPTY.value();

        if (context.has(ComponentTypes.WORLD, ComponentTypes.POS)) {
            World world = context.get(ComponentTypes.WORLD);
            Vector2 pos = context.get(ComponentTypes.POS);
            FastNoiseLite fastNoiseLite = new FastNoiseLite(noise).seed(world.getSeed());
            double n = fastNoiseLite.getNoise(pos.x, pos.y);
            for (Entry entry : entries) {
                if (n <= entry.max()) {
                    TileProvider tileProvider = entry.tile;
                    if (tileProvider != SimpleTileProvider.EMPTY) {
                        return tileProvider.get(context);
                    }
                }
            }
        }

        return SimpleTileProvider.EMPTY.value();
    }

    record Entry(float max, TileProvider tile) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("max").forGetter(Entry::max),
            TileProvider.CODEC.fieldOf("tile").forGetter(Entry::tile)
        ).apply(instance, Entry::new));
    }
}
