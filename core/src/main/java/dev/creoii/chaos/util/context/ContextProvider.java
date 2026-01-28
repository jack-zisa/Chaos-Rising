package dev.creoii.chaos.util.context;

import dev.creoii.chaos.Game;

import javax.annotation.Nullable;

public interface ContextProvider {
    default Game getGame() {
        return getContext().getGame();
    }

    Context getContext();

    default ContextProvider getParent() {
        return getContext().getParent();
    }

    @Nullable
    default <T> T get(ComponentType<T> type) {
        return getContext().get(type);
    }

    @Nullable
    default <T> T get(ComponentType<T> type, @Nullable T fallback) {
        return getContext().has(type) ? getContext().get(type) : fallback;
    }

    default <T> void set(ComponentType<T> type, T value) {
        getContext().set(type, value);
    }

    default <T> Context with(ComponentType<T> type, T value) {
        return getContext().with(type, value);
    }

    default <T> boolean has(ComponentType<T> type) {
        return getContext().has(type) || getParent().has(type);
    }

    default boolean has(ComponentType<?>... types) {
        for (ComponentType<?> type : types) {
            if (!has(type))
                return false;
        }
        return true;
    }

    default void clearLocal() {
        getContext().clearLocal();
    }

    default Context child() {
        return getContext().child();
    }
}
