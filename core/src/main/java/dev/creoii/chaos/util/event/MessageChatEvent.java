package dev.creoii.chaos.util.event;

import dev.creoii.chaos.Game;
import dev.creoii.chaos.chat.Message;

@FunctionalInterface
public interface MessageChatEvent {
    Event<MessageChatEvent> EVENT = Event.create(MessageChatEvent.class, events -> (game, message) -> {
        for (MessageChatEvent event : events) {
            event.onMessageChat(game, message);
        }
    });

    void onMessageChat(Game game, Message message);
}
