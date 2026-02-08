package dev.creoii.chaos.entity.behavior.action;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.attack.Attack;
import dev.creoii.chaos.entity.Attacker;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.Context;
import dev.creoii.chaos.util.provider.entityprovider.SelfEntityProvider;
import dev.creoii.chaos.util.provider.vecprovider.ConstantVecProvider;
import dev.creoii.chaos.util.provider.vecprovider.EntityVecProvider;

public class AttackAction extends Action {
    public static final MapCodec<AttackAction> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Attack.CODEC.fieldOf("attack").forGetter(AttackAction::getAttack)
        ).apply(instance, AttackAction::new);
    });
    private final Attack attack;

    public AttackAction(Attack attack) {
        this.attack = attack;
    }

    @Override
    public Type getType() {
        return Type.ATTACK;
    }

    public Attack getAttack() {
        return attack;
    }

    @Override
    public void start(EntityController<? extends EnemyEntity> controller) {
    }

    @Override
    public void update(EntityController<? extends EnemyEntity> controller, int time, float delta) {
        Entity entity = controller.getEntity();
        if (Attacker.canAttack(entity)) {
            Context context = Context.rootOf(entity);
            context.set(ComponentTypes.TARGET_POS, entity.getPos());
            attack.attack(new ConstantVecProvider(entity.getPos()), entity, null, context);
        }
    }

    @Override
    public void end(EntityController<? extends EnemyEntity> controller) {
    }

    @Override
    public boolean isInstant() {
        return false;
    }
}
