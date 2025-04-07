package dev.creoii.chaos.entity.ai.controller;

import dev.creoii.chaos.InputManager;
import dev.creoii.chaos.entity.Entity;

public abstract class EntityController<T extends Entity> {
    protected final T entity;
    private final InputManager inputManager;

    protected EntityController(T entity) {
        this.entity = entity;
        inputManager = entity.getGame().getInputManager();
    }

    public abstract void control(float delta);

    public T getEntity() {
        return entity;
    }

    public InputManager getInputManager() {
        return inputManager;
    }
}
