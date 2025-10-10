package dev.creoii.chaos.util;

import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.controller.EntityController;

@FunctionalInterface
public interface Controllable {
    EntityController<? extends Entity> getController();
}
