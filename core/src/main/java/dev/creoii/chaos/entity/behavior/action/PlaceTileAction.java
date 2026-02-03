package dev.creoii.chaos.entity.behavior.action;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.util.context.Context;
import dev.creoii.chaos.util.provider.tileprovider.TileProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public class PlaceTileAction extends InstantAction {
    public static final MapCodec<PlaceTileAction> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            TileProvider.CODEC.fieldOf("tile").forGetter(PlaceTileAction::getTile),
            VecProvider.CODEC.fieldOf("offset").forGetter(PlaceTileAction::getOffset)
        ).apply(instance, PlaceTileAction::new);
    });
    private final TileProvider tile;
    private final VecProvider offset;

    public PlaceTileAction(TileProvider tile, VecProvider offset) {
        this.tile = tile;
        this.offset = offset;
    }

    public TileProvider getTile() {
        return tile;
    }

    public VecProvider getOffset() {
        return offset;
    }

    @Override
    public Type getType() {
        return Type.PLACE_TILE;
    }

    @Override
    public void start(EntityController<? extends EnemyEntity> controller) {
        EnemyEntity entity = controller.getEntity();
        Context context = Context.rootOf(entity);
        int x = Math.round(entity.getPos().x / Entity.COORDINATE_SCALE);
        int y = Math.round(entity.getPos().y / Entity.COORDINATE_SCALE);
        Vector2 offset = this.offset.get(context);
        entity.getWorld().setGround(x + (int) offset.x, y + (int) offset.y, tile.get(context));
    }
}
