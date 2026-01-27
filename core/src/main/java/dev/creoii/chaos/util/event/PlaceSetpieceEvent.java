package dev.creoii.chaos.util.event;

import dev.creoii.chaos.World;
import dev.creoii.chaos.world.map.Setpiece;

@FunctionalInterface
public interface PlaceSetpieceEvent {
    Event<PlaceSetpieceEvent> EVENT = Event.create(PlaceSetpieceEvent.class, events -> (world, setpiece, x, y) -> {
        for (PlaceSetpieceEvent event : events) {
            event.onSpawnEntity(world, setpiece, x, y);
        }
    });

    void onSpawnEntity(World world, Setpiece setpiece, int x, int y);
}
