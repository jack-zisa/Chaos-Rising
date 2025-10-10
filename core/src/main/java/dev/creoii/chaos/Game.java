package dev.creoii.chaos;

import com.esotericsoftware.kryonet.Server;

import java.io.Serializable;

public interface Game extends Serializable {
    boolean isClient();

    OptionsManager getOptionsManager();

    EntityManager<?> getEntityManager();

    int getGametime();

    Server getServer();
}
