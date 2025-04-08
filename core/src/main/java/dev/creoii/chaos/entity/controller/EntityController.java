package dev.creoii.chaos.entity.controller;

import dev.creoii.chaos.entity.Entity;

public abstract class EntityController<T extends Entity> {
    protected final T entity;

    protected EntityController(T entity) {
        this.entity = entity;
    }

    public abstract void control(float delta);

    public T getEntity() {
        return entity;
    }
}
