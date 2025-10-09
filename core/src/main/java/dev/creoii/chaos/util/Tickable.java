package dev.creoii.chaos.util;

public interface Tickable {
    void tick(int gametime, float delta);

    default int getTickRate() {
        return 1;
    }
}
