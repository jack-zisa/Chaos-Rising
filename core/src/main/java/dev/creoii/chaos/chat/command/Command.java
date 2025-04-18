package dev.creoii.chaos.chat.command;

import dev.creoii.chaos.Game;

import java.util.function.BiConsumer;

public record Command(BiConsumer<Game, String[]> executor) {
    public void execute(Game game, String[] args) {
        executor.accept(game, args);
    }

    static void register(String id, BiConsumer<Game, String[]> executor) {
        Commands.ALL.put(id, new Command(executor));
    }
}
