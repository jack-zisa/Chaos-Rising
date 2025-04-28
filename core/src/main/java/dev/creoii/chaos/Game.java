package dev.creoii.chaos;

import java.io.Serializable;

public interface Game extends Serializable {
    OptionsManager getOptionsManager();

    DataManager getDataManager();

    EntityManager getEntityManager();

    int getGametime();
}
