package dev.creoii.chaos.util.provider;

import dev.creoii.chaos.Game;

public interface Provider<T> {
    T get(Game game);

    Provider<T> copy();
}
