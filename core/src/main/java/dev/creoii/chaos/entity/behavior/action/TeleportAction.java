package dev.creoii.chaos.entity.behavior.action;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.network.s2c.MoveEntityS2C;
import dev.creoii.chaos.util.context.Context;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public class TeleportAction extends Action {
    public static final MapCodec<TeleportAction> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("pos").forGetter(TeleportAction::getPos)
        ).apply(instance, pos -> new TeleportAction((VecProvider) pos.optimize()));
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
        Vector2 pos = this.pos.get(Context.rootOf(controller.getEntity()));
        controller.getEntity().setPos(pos.x, pos.y);
        if (!controller.getEntity().getWorld().getGame().isClient()) {
            controller.getEntity().getWorld().getGame().getServer().sendToAllTCP(new MoveEntityS2C(controller.getEntity().getId(), pos.x, pos.y, 0f, 0f));
        }
    }

    @Override
    public void update(EntityController<? extends EnemyEntity> controller, int time, float delta) {
    }

    @Override
    public void end(EntityController<? extends EnemyEntity> controller) {
    }
}
