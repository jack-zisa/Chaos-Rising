package dev.creoii.chaos;

import com.esotericsoftware.kryonet.Server;

public interface Game {
    boolean isClient();

    EntityManager<?> getEntityManager();

    int getGametime();

    Server getServer();
}
