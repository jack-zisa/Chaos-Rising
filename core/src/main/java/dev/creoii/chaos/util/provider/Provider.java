package dev.creoii.chaos.util.provider;

import dev.creoii.chaos.util.context.ContextProvider;

public interface Provider<T> {
    T get(ContextProvider context);

    default Provider<T> optimize() {
        return this;
    }
}
