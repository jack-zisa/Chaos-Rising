package dev.creoii.chaos.chat.command;

import dev.creoii.chaos.ClientGame;

import java.util.function.BiConsumer;

public record Command(BiConsumer<ClientGame, String[]> executor) {
    public void execute(ClientGame game, String[] args) {
        executor.accept(game, args);
    }

    static void register(String id, BiConsumer<ClientGame, String[]> executor) {
        Commands.ALL.put(id, new Command(executor));
    }
}
