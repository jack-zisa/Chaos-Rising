package dev.creoii.chaos;

import java.io.Serializable;

public interface Game extends Serializable {
    boolean isClient();

    OptionsManager getOptionsManager();

    DataManager getDataManager();

    EntityManager<?> getEntityManager();

    int getGametime();
}
