package dev.creoii.chaos.entity.behavior.action;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.util.context.Context;
import dev.creoii.chaos.util.provider.stringprovider.StringProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public class PlaceSetpieceAction extends InstantAction {
    public static final MapCodec<PlaceSetpieceAction> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            StringProvider.CODEC.fieldOf("setpiece").forGetter(PlaceSetpieceAction::getSetpiece),
            VecProvider.CODEC.fieldOf("offset").forGetter(PlaceSetpieceAction::getOffset)
        ).apply(instance, PlaceSetpieceAction::new);
    });
    private final StringProvider setpiece;
    private final VecProvider offset;

    public PlaceSetpieceAction(StringProvider setpiece, VecProvider offset) {
            this.setpiece = setpiece;
        this.offset = offset;
    }

    public StringProvider getSetpiece() {
        return setpiece;
    }

    public VecProvider getOffset() {
        return offset;
    }

    @Override
    public Type getType() {
        return Type.PLACE_SETPIECE;
    }

    @Override
    public void start(EntityController<? extends EnemyEntity> controller) {
        EnemyEntity entity = controller.getEntity();
        Context context = Context.rootOf(entity);
        int x = Math.round(entity.getPos().x / Entity.COORDINATE_SCALE);
        int y = Math.round(entity.getPos().y / Entity.COORDINATE_SCALE);
        Vector2 offset = this.offset.get(context);
        entity.getWorld().placeSetpiece(DataManager.getSetpiece(setpiece.get(context)), x + (int) offset.x, y + (int) offset.y);
    }
}
