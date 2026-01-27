package dev.creoii.chaos;

import com.esotericsoftware.kryonet.Server;

import java.util.Random;

public interface Game {
    boolean isClient();

    int getGametime();

    Random getRandom();

    Server getServer();
}
