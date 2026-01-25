package dev.creoii.chaos.util.event;

import dev.creoii.chaos.Game;

@FunctionalInterface
public interface GainExperienceEvent {
    Event<GainExperienceEvent> EVENT = Event.create(GainExperienceEvent.class, events -> (game, entity, experience) -> {
        for (GainExperienceEvent event : events) {
            event.onGainExperience(game, entity, experience);
        }
    });

    void onGainExperience(Game game, int entity, int experience);
}
