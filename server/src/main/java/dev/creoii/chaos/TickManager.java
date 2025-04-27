package dev.creoii.chaos;

import dev.creoii.chaos.util.Tickable;

import java.util.ArrayList;
import java.util.List;

public class TickManager {
    private final ServerGame game;
    private final List<Tickable> tickables;

    public TickManager(ServerGame game) {
        this.game = game;
        tickables = new ArrayList<>();
    }

    public void tick(int gametime, float delta) {
        for (int i = tickables.size() - 1; i >= 0; --i) {
            tickables.get(i).tick(gametime, delta);
        }
    }

    public void addTickable(Tickable tickable) {
        tickables.add(tickable);
    }

    public void removeTickable(Tickable tickable) {
        tickables.remove(tickable);
    }
}
