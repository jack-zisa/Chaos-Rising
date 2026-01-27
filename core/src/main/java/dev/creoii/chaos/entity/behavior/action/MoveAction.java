package dev.creoii.chaos.entity.behavior.action;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public class MoveAction extends Action {
    public static final MapCodec<MoveAction> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("movement").forGetter(MoveAction::getMovement)
        ).apply(instance, movement -> new MoveAction((VecProvider) movement.optimize()));
    });
    private final VecProvider movement;
    private Provider.Context context;
    private float speed;

    public MoveAction(VecProvider movement) {
        this.movement = movement;
    }

    @Override
    public Type getType() {
        return Type.MOVE;
    }

    public VecProvider getMovement() {
        return movement;
    }

    @Override
    public void start(EntityController<? extends EnemyEntity> controller) {
        context = Provider.Context.of(controller.getEntity(), controller.getEntity().getWorld().getGame().getGametime());
        speed = (controller.getEntity() instanceof LivingEntity living ? living.getStats().speed().value() : 1f);
    }

    @Override
    public void update(EntityController<? extends EnemyEntity> controller, int time, float delta) {
        if (context == null) {
            context = Provider.Context.of(controller.getEntity(), controller.getEntity().getWorld().getGame().getGametime());
        }
        context.setTime(controller.getEntity().getWorld().getGame().getGametime());
        Vector2 move = movement.get(context);
        controller.getEntity().getPos().add(move.scl(speed * delta));
    }

    @Override
    public void end(EntityController<? extends EnemyEntity> controller) {
        speed = (controller.getEntity() instanceof LivingEntity living ? living.getStats().speed().value() : 1f);
    }
}
