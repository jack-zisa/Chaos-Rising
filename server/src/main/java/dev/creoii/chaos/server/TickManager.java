package dev.creoii.chaos.server;

import dev.creoii.chaos.util.Tickable;

import java.util.ArrayList;
import java.util.List;

public class TickManager {
    private final List<Tickable> tickables;

    public TickManager() {
        tickables = new ArrayList<>();
    }

    public void tick(int gametime, float delta) {
        for (int i = tickables.size() - 1; i >= 0; --i) {
            Tickable tickable = tickables.get(i);
            if (gametime % tickable.getTickRate() == 0)
                tickable.tick(gametime, delta);
        }
    }

    public void addTickable(Tickable tickable) {
        tickables.add(tickable);
    }

    public void removeTickable(Tickable tickable) {
        tickables.remove(tickable);
    }
}
