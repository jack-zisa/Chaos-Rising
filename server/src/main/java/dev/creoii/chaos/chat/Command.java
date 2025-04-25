package dev.creoii.chaos.chat;

import dev.creoii.chaos.ServerGame;
import dev.creoii.chaos.util.function.TriConsumer;

import java.util.UUID;

public record Command(TriConsumer<ServerGame, UUID, String[]> executor) {
    public void execute(ServerGame game, UUID uuid, String[] args) {
        executor.accept(game, uuid, args);
    }

    static void register(String id, TriConsumer<ServerGame, UUID, String[]> executor) {
        Commands.ALL.put(id, new Command(executor));
    }
}
