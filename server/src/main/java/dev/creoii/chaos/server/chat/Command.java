package dev.creoii.chaos.server.chat;

import dev.creoii.chaos.server.ServerGame;
import dev.creoii.chaos.util.function.TriFunction;

import java.util.Arrays;
import java.util.function.BiFunction;

public record Command(TriFunction<ServerGame, Integer, String[], Result> executor) {
    public Result execute(ServerGame game, int id, String[] args) {
        return executor.apply(game, id, args);
    }

    static void register(String id, TriFunction<ServerGame, Integer, String[], Result> executor) {
        Commands.ALL.put(id, new Command(executor));
    }

    public enum Result {
        SUCCESS((commandType, args) -> "[Commands] Successfully executed '/" + commandType + "' with args " + Arrays.toString(args)),
        FAIL((commandType, args) -> "[Commands] Execution '/" + commandType + "' with args " + Arrays.toString(args) + " failed");

        private final BiFunction<String, String[], String> message;

        Result(BiFunction<String, String[], String> message) {
            this.message = message;
        }

        public String getResultMessage(String commandType, String[] args) {
            return message.apply(commandType, args);
        }

        public String getResultMessageWithReason(String commandType, String[] args, String reason) {
            return message.apply(commandType, args) + ": " + reason;
        }
    }
}
