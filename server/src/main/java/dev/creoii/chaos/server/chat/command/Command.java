package dev.creoii.chaos.server.chat.command;

import com.badlogic.gdx.graphics.Color;
import dev.creoii.chaos.server.ServerWorld;
import dev.creoii.chaos.util.function.TriFunction;

import java.util.Arrays;
import java.util.function.BiFunction;

public record Command(TriFunction<ServerWorld, Integer, String[], Result> executor, int minArgs) {
    public Result execute(ServerWorld world, int id, String[] args) {
        return executor.apply(world, id, args);
    }

    static void register(String id, int minArgs, TriFunction<ServerWorld, Integer, String[], Result> executor) {
        Commands.ALL.put(id, new Command(executor, minArgs));
    }

    public enum Result {
        SUCCESS((commandType, args) -> "[Commands] Successfully executed '/" + commandType + "' with args '" + Arrays.toString(args) + "'"),
        FAIL((commandType, args) -> "[Commands] Execution of '/" + commandType + "' with args '" + Arrays.toString(args) + "' failed");

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

        public static Color getChatMessageColor(Result result) {
            return result == SUCCESS ? Color.WHITE : Color.RED;
        }
    }
}
