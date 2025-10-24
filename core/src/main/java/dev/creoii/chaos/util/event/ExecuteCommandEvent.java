package dev.creoii.chaos.util.event;

import dev.creoii.chaos.Game;

@FunctionalInterface
public interface ExecuteCommandEvent {
    Event<ExecuteCommandEvent> EVENT = Event.create(ExecuteCommandEvent.class, events -> (game, entity, command, args) -> {
        for (ExecuteCommandEvent event : events) {
            event.onExecuteCommand(game, entity, command, args);
        }
    });

    void onExecuteCommand(Game game, int entity, String command, String[] args);
}
