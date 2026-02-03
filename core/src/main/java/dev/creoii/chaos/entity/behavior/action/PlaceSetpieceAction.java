package dev.creoii.chaos.entity.behavior.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.controller.EntityController;

public class PlaceSetpieceAction extends InstantAction {
    public static final MapCodec<PlaceSetpieceAction> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("setpiece").forGetter(PlaceSetpieceAction::getSetpiece)
        ).apply(instance, PlaceSetpieceAction::new);
    });
    private final String setpiece;

    public PlaceSetpieceAction(String setpiece) {
            this.setpiece = setpiece;
    }

    public String getSetpiece() {
        return setpiece;
    }

    @Override
    public Type getType() {
        return Type.PLACE_SETPIECE;
    }

    @Override
    public void start(EntityController<? extends EnemyEntity> controller) {
        int x = Math.round(controller.getEntity().getPos().x / Entity.COORDINATE_SCALE);
        int y = Math.round(controller.getEntity().getPos().y / Entity.COORDINATE_SCALE);
        controller.getEntity().getWorld().placeSetpiece(DataManager.getSetpiece(setpiece), x, y);
    }
}
