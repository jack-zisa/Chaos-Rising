package dev.creoii.chaos;

import com.esotericsoftware.kryonet.Server;

import java.util.Random;

public interface Game {
    boolean isClient();

    EntityManager<?> getEntityManager();

    int getGametime();

    Random getRandom();

    Server getServer();

    World getWorld();
}
