package dev.creoii.chaos.util.event;

import dev.creoii.chaos.World;

@FunctionalInterface
public interface ExecuteCommandEvent {
    Event<ExecuteCommandEvent> EVENT = Event.create(ExecuteCommandEvent.class, events -> (world, entity, command, args) -> {
        for (ExecuteCommandEvent event : events) {
            event.onExecuteCommand(world, entity, command, args);
        }
    });

    void onExecuteCommand(World world, int entity, String command, String[] args);
}
