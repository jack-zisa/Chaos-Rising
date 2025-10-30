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
        ).apply(instance, MoveAction::new);
    });
    private final VecProvider movement;
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
        speed = (controller.getEntity() instanceof LivingEntity living ? living.getStats().speed().value() : 1f);
    }

    @Override
    public void update(EntityController<? extends EnemyEntity> controller, int time, float delta) {
        Vector2 move = movement.get(Provider.Context.of(controller.getEntity(), controller.getEntity().getGame().getGametime()));
        controller.getEntity().getPos().add(move.scl(speed * delta));
    }

    @Override
    public void reset(EntityController<? extends EnemyEntity> controller) {
        speed = (controller.getEntity() instanceof LivingEntity living ? living.getStats().speed().value() : 1f);
    }
}
