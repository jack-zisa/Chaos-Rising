package dev.creoii.chaos.entity.behavior.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.attack.Attack;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.util.provider.vecprovider.TargetPosVecProvider;

public class AttackAction extends Action {
    public static final MapCodec<AttackAction> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Attack.CODEC.fieldOf("attack").forGetter(AttackAction::getAttack),
            Codec.INT.fieldOf("cooldown").forGetter(AttackAction::getAttackCooldown)
        ).apply(instance, AttackAction::new);
    });
    private final Attack attack;
    private int attackCooldown;

    public AttackAction(Attack attack, int cooldown) {
        this.attack = attack;
        this.attackCooldown = cooldown;
    }

    @Override
    public Type getType() {
        return Type.ATTACK;
    }

    public Attack getAttack() {
        return attack;
    }

    public int getAttackCooldown() {
        return attackCooldown;
    }

    @Override
    public void update(EntityController<? extends EnemyEntity> controller, int time, float delta) {
        if (--attackCooldown <= 0) {
            attack.attack(new TargetPosVecProvider(), controller.getEntity());
            //attackCooldown = getData().getInt("cooldown");
        }
    }

    @Override
    public void reset(EntityController<? extends EnemyEntity> controller) {
        //attackCooldown = getData().getInt("cooldown");
    }
}
