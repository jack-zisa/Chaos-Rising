package dev.creoii.chaos.client.option;

public interface Option<T> {
    String key();

    T value();

    void setValue(T value);
}
