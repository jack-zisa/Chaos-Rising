package dev.creoii.chaos.util.event;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectSet;

import java.lang.reflect.Array;
import java.util.function.Function;

public class Event<T> {
    private final ObjectSet<T> listeners = new ObjectArraySet<>();
    private final Function<T[], T> invokerFactory;
    private final Class<T> type;
    private volatile T invoker;

    @SuppressWarnings("unchecked")
    private Event(Class<T> type, Function<T[], T> invokerFactory) {
        this.type = type;
        this.invokerFactory = invokerFactory;
        this.invoker = invokerFactory.apply((T[]) Array.newInstance(type, 0));
    }

    public static <T> Event<T> create(Class<T> type, Function<T[], T> invokerFactory) {
        return new Event<>(type, invokerFactory);
    }

    public void register(T listener) {
        listeners.add(listener);
        update();
    }

    public T invoker() {
        return invoker;
    }

    @SuppressWarnings("unchecked")
    private void update() {
        T[] array = listeners.toArray((T[]) Array.newInstance(type, listeners.size()));
        invoker = invokerFactory.apply(array);
    }
}
