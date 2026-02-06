package dev.creoii.chaos.util.provider.tileprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.World;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.util.provider.entityprovider.EntityProvider;
import dev.creoii.chaos.world.tile.Tile;

public record AtEntityPosTileProvider(EntityProvider entity) implements TileProvider {
    public static final MapCodec<AtEntityPosTileProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        EntityProvider.CODEC.fieldOf("entity").forGetter(AtEntityPosTileProvider::entity)
    ).apply(instance, AtEntityPosTileProvider::new));

    @Override
    public Type getType() {
        return Type.AT_ENTITY_POS;
    }

    @Override
    public Tile get(ContextProvider context) {
        if (context.has(ComponentTypes.WORLD)) {
            World world = context.get(ComponentTypes.WORLD);
            Entity entity = entity().get(context);
            int x = Math.round(entity.getPos().x / Entity.COORDINATE_SCALE);
            int y = Math.round(entity.getPos().y / Entity.COORDINATE_SCALE);
            return world.getGround(x, y);
        }

        return SimpleTileProvider.EMPTY.value();
    }
}
