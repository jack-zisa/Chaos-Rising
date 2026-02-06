package dev.creoii.chaos.server.chat;

import dev.creoii.chaos.ChatManager;
import dev.creoii.chaos.chat.Message;
import dev.creoii.chaos.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public record ServerChatManager(ServerWorld world, List<Message> messages) implements ChatManager {
    public ServerChatManager(ServerWorld world) {
        this(world, new ArrayList<>());
    }
}
