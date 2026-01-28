package dev.creoii.chaos.util.context;

public final class ComponentType<T> {
    private static int NEXT_ID = 0;

    public final int id;

    private ComponentType() {
        id = NEXT_ID++;
    }

    public static <T> ComponentType<T> create() {
        return new ComponentType<>();
    }

    public static int count() {
        return NEXT_ID;
    }
}
