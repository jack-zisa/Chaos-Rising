package dev.creoii.chaos.util.event;

import dev.creoii.chaos.World;

@FunctionalInterface
public interface DamageEntityEvent {
    Event<DamageEntityEvent> EVENT = Event.create(DamageEntityEvent.class, events -> (world, amount, entity, attacker) -> {
        for (DamageEntityEvent event : events) {
            event.onDamageEntity(world, amount, entity, attacker);
        }
    });

    void onDamageEntity(World world, float amount, int entity, int attacker);
}
