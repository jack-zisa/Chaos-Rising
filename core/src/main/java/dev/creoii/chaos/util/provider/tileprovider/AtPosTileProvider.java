package dev.creoii.chaos.util.provider.tileprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.World;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;
import dev.creoii.chaos.world.tile.Tile;

public record AtPosTileProvider(VecProvider pos) implements TileProvider {
    public static final MapCodec<AtPosTileProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        VecProvider.CODEC.fieldOf("pos").forGetter(AtPosTileProvider::pos)
    ).apply(instance, AtPosTileProvider::new));

    @Override
    public Type getType() {
        return Type.AT_POS;
    }

    @Override
    public Tile get(ContextProvider context) {
        if (context.has(ComponentTypes.WORLD)) {
            World world = context.get(ComponentTypes.WORLD);
            Vector2 pos = pos().get(context);
            return world.getGround((int) pos.x, (int) pos.y);
        }

        return SimpleTileProvider.EMPTY.value();
    }
}
