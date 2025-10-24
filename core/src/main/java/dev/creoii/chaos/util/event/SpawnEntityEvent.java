package dev.creoii.chaos.util.event;

import dev.creoii.chaos.Game;

@FunctionalInterface
public interface SpawnEntityEvent {
    Event<SpawnEntityEvent> EVENT = Event.create(SpawnEntityEvent.class, events -> (game, entity) -> {
        for (SpawnEntityEvent event : events) {
            event.onSpawnEntity(game, entity);
        }
    });

    void onSpawnEntity(Game game, int entity);
}
