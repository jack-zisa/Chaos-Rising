package dev.creoii.chaos.entity.behavior.action;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.attack.Attack;
import dev.creoii.chaos.entity.Attacker;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.util.provider.entityprovider.NearestCharacterEntityProvider;
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
        if (Attacker.canAttack(controller.getEntity())) {
            attack.attack(new EntityVecProvider(NearestCharacterEntityProvider.INSTANCE), controller.getEntity(), null);
        }
    }

    @Override
    public void reset(EntityController<? extends EnemyEntity> controller) {
    }
}
