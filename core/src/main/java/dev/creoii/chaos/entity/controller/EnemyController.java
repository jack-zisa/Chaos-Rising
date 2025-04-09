package dev.creoii.chaos.entity.controller;

import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.ai.movement.Movements;

import java.util.HashMap;
import java.util.Map;

public class EnemyController extends EntityController<EnemyEntity> {
    Map<String, Object> data = new HashMap<>();

    public EnemyController(EnemyEntity entity) {
        super(entity);
        data.put("speed", 4f);
    }


    @Override
    public void control(int gametime, float delta) {
        Movements.BEHAVIORS.get("chase").accept(entity, delta, data);
    }
}
