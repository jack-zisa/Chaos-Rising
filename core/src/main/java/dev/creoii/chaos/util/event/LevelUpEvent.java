package dev.creoii.chaos.util.event;

import dev.creoii.chaos.World;

@FunctionalInterface
public interface LevelUpEvent {
    Event<LevelUpEvent> EVENT = Event.create(LevelUpEvent.class, events -> (world, entity, level) -> {
        for (LevelUpEvent event : events) {
            event.onLevelUp(world, entity, level);
        }
    });

    void onLevelUp(World world, int entity, int level);
}
