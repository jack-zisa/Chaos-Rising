package dev.creoii.chaos.world.setpiece;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.World;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;
import dev.creoii.chaos.util.provider.tileprovider.TileProvider;

public record RandomWalkSetpiece(String id, String layer, TileProvider tile, NumberProvider steps) implements Setpiece {
    public static final MapCodec<RandomWalkSetpiece> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.STRING.fieldOf("id").forGetter(RandomWalkSetpiece::id),
        Codec.STRING.fieldOf("layer").orElse("ground").forGetter(RandomWalkSetpiece::layer),
        TileProvider.CODEC.fieldOf("tile").forGetter(RandomWalkSetpiece::tile),
        NumberProvider.CODEC.fieldOf("steps").forGetter(RandomWalkSetpiece::steps)
    ).apply(instance, RandomWalkSetpiece::new));

    @Override
    public Type getType() {
        return Type.LAYERED_AREA;
    }

    public void place(World world, int x, int y) {
        int steps = this.steps.getInt(new Provider.Context(world.getGame(), world, null, world.getGame().getGametime(), new Vector2(x, y), world.getRandom()));
        for (int i = 0; i < steps; ++i) {
            int direction = world.getRandom().nextInt(4);

            if (direction == 0) ++x;
            else if (direction == 1) --x;
            else if (direction == 2) ++y;
            else --y;

            if ("ground".equals(layer)) {
                world.setGround(x, y, tile.get(new Provider.Context(world.getGame(), world, null, world.getGame().getGametime(), new Vector2(x, y), world.getRandom())));
            } else if ("object".equals(layer)) {
                world.setObject(x, y, tile.get(new Provider.Context(world.getGame(), world, null, world.getGame().getGametime(), new Vector2(x, y), world.getRandom())));
            }
        }
    }
}
