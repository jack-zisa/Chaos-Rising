package dev.creoii.chaos.util.event;

import dev.creoii.chaos.World;

@FunctionalInterface
public interface SpawnEntityEvent {
    Event<SpawnEntityEvent> EVENT = Event.create(SpawnEntityEvent.class, events -> (world, entity) -> {
        for (SpawnEntityEvent event : events) {
            event.onSpawnEntity(world, entity);
        }
    });

    void onSpawnEntity(World world, int entity);
}
