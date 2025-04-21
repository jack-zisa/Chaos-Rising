package dev.creoii.chaos.entity.behavior.action;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.attack.Attack;
import dev.creoii.chaos.entity.controller.EnemyController;
import dev.creoii.chaos.util.provider.vecprovider.TargetPosVecProvider;

public class AttackAction extends Action {
    private final Attack attack;
    private int attackCooldown;

    public AttackAction(Attack attack, JsonValue data) {
        super(data);
        this.attack = attack;
        this.attackCooldown = data.getInt("cooldown");
    }

    @Override
    public void update(EnemyController controller, int time, float delta) {
        if (--attackCooldown <= 0) {
            attack.attack(new TargetPosVecProvider(), controller.getEntity());
            attackCooldown = getData().getInt("cooldown");
        }
    }

    @Override
    public void reset(EnemyController controller) {
        attackCooldown = getData().getInt("cooldown");
    }
}
