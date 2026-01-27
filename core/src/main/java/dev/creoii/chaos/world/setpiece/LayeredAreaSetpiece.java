package dev.creoii.chaos.world.setpiece;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.World;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.tileprovider.SimpleTileProvider;
import dev.creoii.chaos.util.provider.tileprovider.TileProvider;
import dev.creoii.chaos.world.Layer;
import dev.creoii.chaos.world.Palette;

import java.util.List;
import java.util.Map;

public record LayeredAreaSetpiece(String id, Palette palette, Map<String, Layer> layers) implements Setpiece {
    public static final MapCodec<LayeredAreaSetpiece> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.STRING.fieldOf("id").forGetter(LayeredAreaSetpiece::id),
        Palette.CODEC.fieldOf("palette").forGetter(LayeredAreaSetpiece::palette),
        Codec.unboundedMap(Codec.STRING, Layer.CODEC).fieldOf("layers").forGetter(LayeredAreaSetpiece::layers)
    ).apply(instance, LayeredAreaSetpiece::new));

    @Override
    public Type getType() {
        return Type.LAYERED_AREA;
    }

    public void place(World world, int x, int y) {
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
                            world.setGround(ri + x, ci + y, tile.get(new Provider.Context(world.getGame(), world, null, world.getGame().getGametime(), new Vector2(ri + x, ci + y), world.getRandom())));
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
                            world.setObject(ri + x, ci + y, tile.get(new Provider.Context(world.getGame(), world, null, world.getGame().getGametime(), new Vector2(ri + x, ci + y), world.getRandom())));
                    }
                }
            }
        });
    }
}
