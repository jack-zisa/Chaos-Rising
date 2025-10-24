package dev.creoii.chaos.util.event;

import dev.creoii.chaos.Game;

@FunctionalInterface
public interface DamageEntityEvent {
    Event<DamageEntityEvent> EVENT = Event.create(DamageEntityEvent.class, events -> (game, amount, entity, attacker) -> {
        for (DamageEntityEvent event : events) {
            event.onDamageEntity(game, amount, entity, attacker);
        }
    });

    void onDamageEntity(Game game, float amount, int entity, int attacker);
}
