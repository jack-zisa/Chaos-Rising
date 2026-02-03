package dev.creoii.chaos.entity.behavior.action;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.Context;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public class MoveAction extends Action implements ContextProvider {
    public static final MapCodec<MoveAction> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("movement").forGetter(MoveAction::getMovement)
        ).apply(instance, movement -> new MoveAction((VecProvider) movement.optimize()));
    });
    private final VecProvider movement;
    private float speed;
    private Context context;

    public MoveAction(VecProvider movement) {
        this.movement = movement;
        context = null;
    }

    @Override
    public Type getType() {
        return Type.MOVE;
    }

    public VecProvider getMovement() {
        return movement;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void start(EntityController<? extends EnemyEntity> controller) {
        context = Context.rootOf(controller.getEntity());
        speed = (controller.getEntity() instanceof LivingEntity living ? living.getStats().speed().value() : 1f);
    }

    @Override
    public void update(EntityController<? extends EnemyEntity> controller, int time, float delta) {
        if (context == null) {
            return;
        }
        context.set(ComponentTypes.TIME, controller.getEntity().getWorld().getGame().getGametime());
        Vector2 move = movement.get(context).scl(speed * delta);
        controller.getEntity().setVelocity(move.x, move.y);
    }

    @Override
    public void end(EntityController<? extends EnemyEntity> controller) {
        speed = (controller.getEntity() instanceof LivingEntity living ? living.getStats().speed().value() : 1f);
    }
}
