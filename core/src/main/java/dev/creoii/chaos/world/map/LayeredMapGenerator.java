package dev.creoii.chaos.world.map;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.World;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.Context;
import dev.creoii.chaos.util.provider.tileprovider.SimpleTileProvider;
import dev.creoii.chaos.util.provider.tileprovider.TileProvider;
import dev.creoii.chaos.world.Layer;
import dev.creoii.chaos.world.Palette;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record LayeredMapGenerator(String id, Palette palette, Map<String, Layer> layers, float ambientLight) implements MapGenerator {
    public static final LayeredMapGenerator DEFAULT = new LayeredMapGenerator("default", new Palette(Map.of("G", new SimpleTileProvider(DataManager.getTile("grass")))), buildDefaultLayers(), 1f);
    public static final MapCodec<LayeredMapGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.STRING.fieldOf("id").forGetter(LayeredMapGenerator::id),
        Palette.CODEC.fieldOf("palette").forGetter(LayeredMapGenerator::palette),
        Codec.unboundedMap(Codec.STRING, Layer.CODEC).fieldOf("layers").forGetter(LayeredMapGenerator::layers),
        Codec.FLOAT.fieldOf("ambient_light").orElse(1f).forGetter(LayeredMapGenerator::ambientLight)
    ).apply(instance, LayeredMapGenerator::new));

    @Override
    public Type getType() {
        return Type.LAYERED;
    }

    @Override
    public int getWidth() {
        int maxWidth = 0;
        for (Layer layer : layers.values()) {
            for (List<String> list : layer.tiles()) {
                if (list.size() > maxWidth)
                    maxWidth = list.size();
            }
        }
        return maxWidth;
    }

    @Override
    public int getHeight() {
        int maxHeight = 0;
        for (Layer layer : layers.values()) {
            if (layer.tiles().size() > maxHeight)
                maxHeight = layer.tiles().size();
        }
        return maxHeight;
    }

    @Override
    public void build(World world) {

    }

    public void place(World world) {
        Context context = Context.rootOf(world);
        layers().forEach((s, layer) -> {
            Palette palette = palette();
            if ("ground".equals(s)) {
                List<List<String>> row = layer.tiles();
                for (int ri = 0; ri < row.size(); ++ri) {
                    List<String> col = row.get(ri);
                    for (int ci = 0; ci < col.size(); ++ci) {
                        String id = col.get(ci);
                        if (id.isBlank())
                            continue;

                        TileProvider tile = palette.entries().getOrDefault(id, SimpleTileProvider.EMPTY);
                        if (tile != SimpleTileProvider.EMPTY)
                            world.setGround(ri, ci, tile.get(context.child().with(ComponentTypes.POS, new Vector2(ri, ci))));
                    }
                }
            } else if ("object".equals(s)) {
                List<List<String>> row = layer.tiles();
                for (int ri = 0; ri < row.size(); ++ri) {
                    List<String> col = row.get(ri);
                    for (int ci = 0; ci < col.size(); ++ci) {
                        String id = col.get(ci);
                        if (id.isBlank())
                            continue;

                        TileProvider tile = palette.entries().getOrDefault(id, SimpleTileProvider.EMPTY);
                        if (tile != SimpleTileProvider.EMPTY)
                            world.setObject(ri, ci, tile.get(context.child().with(ComponentTypes.POS, new Vector2(ri, ci))));
                    }
                }
            }
        });
    }

    private static Map<String, Layer> buildDefaultLayers() {
        Map<String, Layer> layers = new HashMap<>();
        List<List<String>> ground = new ArrayList<>();
        for (int i = 0; i < 100; ++i) {
            List<String> row = new ArrayList<>();
            for (int j = 0; j < 100; ++j) {
                row.add("G");
            }
            ground.add(row);
        }
        Layer groundLayer = new Layer(ground);
        layers.put("ground", groundLayer);
        layers.put("object", new Layer(new ArrayList<>()));
        return layers;
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
