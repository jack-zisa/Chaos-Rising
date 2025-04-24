package dev.creoii.chaos.util;

public class Mutable<T> {
    private T value;

    public Mutable(T initialValue) {
        value = initialValue;
    }

    public T get() {
        return value;
    }

    public void set(T newValue) {
        value = newValue;
    }
}
