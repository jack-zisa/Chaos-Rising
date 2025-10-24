package dev.creoii.chaos.util.event;

import dev.creoii.chaos.Game;
import dev.creoii.chaos.util.stat.Stat;

@FunctionalInterface
public interface ChangeStatEvent {
    Event<ChangeStatEvent> EVENT = Event.create(ChangeStatEvent.class, events -> (game, entity, stat) -> {
        for (ChangeStatEvent event : events) {
            event.onChangeStat(game, entity, stat);
        }
    });

    void onChangeStat(Game game, int entity, Stat stat);
}
