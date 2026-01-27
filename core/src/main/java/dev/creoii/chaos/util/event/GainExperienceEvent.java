package dev.creoii.chaos.util.event;

import dev.creoii.chaos.World;

@FunctionalInterface
public interface GainExperienceEvent {
    Event<GainExperienceEvent> EVENT = Event.create(GainExperienceEvent.class, events -> (world, entity, experience) -> {
        for (GainExperienceEvent event : events) {
            event.onGainExperience(world, entity, experience);
        }
    });

    void onGainExperience(World world, int entity, int experience);
}
