package dev.creoii.chaos.util.event;

import dev.creoii.chaos.Game;

@FunctionalInterface
public interface LevelUpEvent {
    Event<LevelUpEvent> EVENT = Event.create(LevelUpEvent.class, events -> (game, entity, level) -> {
        for (LevelUpEvent event : events) {
            event.onLevelUp(game, entity, level);
        }
    });

    void onLevelUp(Game game, int entity, int level);
}
