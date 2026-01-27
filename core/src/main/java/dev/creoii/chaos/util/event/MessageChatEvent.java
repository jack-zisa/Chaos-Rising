package dev.creoii.chaos.util.event;

import dev.creoii.chaos.World;
import dev.creoii.chaos.chat.Message;

@FunctionalInterface
public interface MessageChatEvent {
    Event<MessageChatEvent> EVENT = Event.create(MessageChatEvent.class, events -> (world, message) -> {
        for (MessageChatEvent event : events) {
            event.onMessageChat(world, message);
        }
    });

    void onMessageChat(World world, Message message);
}
