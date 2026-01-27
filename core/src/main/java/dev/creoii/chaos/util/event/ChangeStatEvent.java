package dev.creoii.chaos.util.event;

import dev.creoii.chaos.World;
import dev.creoii.chaos.util.stat.Stat;

@FunctionalInterface
public interface ChangeStatEvent {
    Event<ChangeStatEvent> EVENT = Event.create(ChangeStatEvent.class, events -> (world, entity, stat) -> {
        for (ChangeStatEvent event : events) {
            event.onChangeStat(world, entity, stat);
        }
    });

    void onChangeStat(World world, int entity, Stat stat);
}
