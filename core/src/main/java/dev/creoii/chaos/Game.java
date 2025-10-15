package dev.creoii.chaos;

import com.esotericsoftware.kryonet.Server;

public interface Game {
    boolean isClient();

    OptionsManager getOptionsManager();

    EntityManager<?> getEntityManager();

    int getGametime();

    Server getServer();
}
