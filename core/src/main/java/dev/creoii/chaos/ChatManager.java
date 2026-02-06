package dev.creoii.chaos;

import dev.creoii.chaos.chat.Message;

import java.util.Iterator;
import java.util.List;

public interface ChatManager {
    World world();

    List<Message> messages();

    default void update() {
        Iterator<Message> it = messages().iterator();
        while (it.hasNext()) {
            Message message = it.next();
            message.decrementCooldown();
            if (message.getCooldown() <= 0)
                it.remove();
        }
    }
}
