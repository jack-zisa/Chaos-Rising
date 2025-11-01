package dev.creoii.chaos.entity.behavior.action;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public class TeleportAction extends Action {
    public static final MapCodec<TeleportAction> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("pos").forGetter(TeleportAction::getPos)
        ).apply(instance, TeleportAction::new);
    });
    private final VecProvider pos;

    public TeleportAction(VecProvider pos) {
        this.pos = pos;
    }

    @Override
    public Type getType() {
        return Type.TELEPORT;
    }

    public VecProvider getPos() {
        return pos;
    }

    @Override
    public void start(EntityController<? extends EnemyEntity> controller) {
        Vector2 pos = this.pos.get(Provider.Context.of(controller.getEntity(), controller.getEntity().getGame().getGametime()));
        controller.getEntity().setPos(pos.x, pos.y);
    }

    @Override
    public void update(EntityController<? extends EnemyEntity> controller, int time, float delta) {
    }

    @Override
    public void end(EntityController<? extends EnemyEntity> controller) {
    }
}
