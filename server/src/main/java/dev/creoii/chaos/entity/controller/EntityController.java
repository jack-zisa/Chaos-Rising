package dev.creoii.chaos.entity.controller;

import dev.creoii.chaos.entity.ServerEntity;

public abstract class EntityController<T extends ServerEntity> {
    protected T entity;

    protected EntityController(T entity) {
        this.entity = entity;
    }

    public abstract void control(int gametime, float delta);

    public T getEntity() {
        return entity;
    }
}
