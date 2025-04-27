package dev.creoii.chaos;

public interface Game {
    OptionsManager getOptionsManager();

    DataManager getDataManager();

    EntityManager getEntityManager();

    int getGametime();
}
