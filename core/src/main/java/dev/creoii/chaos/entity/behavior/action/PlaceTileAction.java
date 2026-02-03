package dev.creoii.chaos.entity.behavior.action;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.util.context.Context;
import dev.creoii.chaos.util.provider.tileprovider.TileProvider;

public class PlaceTileAction extends InstantAction {
    public static final MapCodec<PlaceTileAction> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            TileProvider.CODEC.fieldOf("tile").forGetter(PlaceTileAction::getTile)
        ).apply(instance, PlaceTileAction::new);
    });
    private final TileProvider tile;

    public PlaceTileAction(TileProvider tile) {
        this.tile = tile;
    }

    public TileProvider getTile() {
        return tile;
    }

    @Override
    public Type getType() {
        return Type.PLACE_TILE;
    }

    @Override
    public void start(EntityController<? extends EnemyEntity> controller) {
        int x = Math.round(controller.getEntity().getPos().x / Entity.COORDINATE_SCALE);
        int y = Math.round(controller.getEntity().getPos().y / Entity.COORDINATE_SCALE);
        controller.getEntity().getWorld().setGround(x, y, tile.get(Context.rootOf(controller.getEntity())));
    }
}
