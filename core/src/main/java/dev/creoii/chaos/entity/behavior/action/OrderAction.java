package dev.creoii.chaos.entity.behavior.action;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.EntityManager;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.entity.behavior.MultiBehavior;
import dev.creoii.chaos.entity.behavior.phase.Phase;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.phaseprovider.PhaseProvider;

public class OrderAction extends Action {
    public static final MapCodec<OrderAction> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            PhaseProvider.CODEC.fieldOf("phase").forGetter(OrderAction::getPhase)
        ).apply(instance, phase -> new OrderAction((PhaseProvider) phase.optimize()));
    });
    private final PhaseProvider phase;

    public OrderAction(PhaseProvider phase) {
        this.phase = phase;
    }

    public PhaseProvider getPhase() {
        return phase;
    }

    @Override
    public Type getType() {
        return Type.ORDER;
    }

    @Override
    public void start(EntityController<? extends EnemyEntity> controller) {
        LivingEntity living = controller.getEntity();
        EntityManager<?> entityManager = living.getWorld().getEntityManager();
        living.getChildren().forEach(integer -> {
            LivingEntity entity = (LivingEntity) entityManager.getEntity(EntityGroup.ENEMY, integer);
            if (entity instanceof EnemyEntity enemy && enemy.getController().getBehavior() instanceof MultiBehavior multiBehavior) {
                Provider.Context context = Provider.Context.of(enemy, enemy.getWorld().getGame().getGametime());
                Phase target = phase.get(context);

                if (multiBehavior.getPhase(target.getId()) == null)
                    return;

                multiBehavior.getCurrentPhase().end(controller);
                multiBehavior.setCurrentPhase(target);
                multiBehavior.getCurrentPhase().start(controller, living.getWorld().getGame().getGametime());
            }
        });
    }

    @Override
    public void update(EntityController<? extends EnemyEntity> controller, int time, float delta) {

    }

    @Override
    public void end(EntityController<? extends EnemyEntity> controller) {

    }
}
