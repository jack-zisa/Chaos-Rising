package dev.creoii.chaos.client.chat;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import dev.creoii.chaos.OptionsManager;
import dev.creoii.chaos.chat.Message;
import dev.creoii.chaos.client.ClientGame;
import dev.creoii.chaos.network.c2s.ChatMessageSendC2S;
import dev.creoii.chaos.network.c2s.ExecuteCommandC2S;
import dev.creoii.chaos.util.event.ExecuteCommandEvent;
import dev.creoii.chaos.util.event.MessageChatEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ChatManager extends InputAdapter {
    private final ClientGame game;
    private final List<Message> messages;
    private final StringBuilder input = new StringBuilder();
    private boolean active = false;
    private boolean suppressNextChar = false;

    public ChatManager(ClientGame game) {
        this.game = game;
        messages = new ArrayList<>();
    }

    public List<Message> getMessages() {
        return messages;
    }

    public StringBuilder getInput() {
        return input;
    }

    public boolean isActive() {
        return active;
    }

    public void executeCommand(String command) {
        String[] elements = command.split(" ");
        String commandType = elements[0].substring(1);

        if (commandType.isEmpty())
            return;

        String[] args = Arrays.copyOfRange(elements, 1, elements.length);
        ExecuteCommandEvent.EVENT.invoker().onExecuteCommand(game, game.getCharacterId(), command, args);
        game.getClient().sendTCP(new ExecuteCommandC2S(game.getCharacter().id, commandType, args));
    }

    public void update() {
        Iterator<Message> it = messages.iterator();
        while (it.hasNext()) {
            Message message = it.next();
            message.decrementCooldown();
            if (message.getCooldown() <= 0)
                it.remove();
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!active) {
            if (keycode == OptionsManager.COMMAND_KEY.intValue()) {
                active = true;
                input.setLength(0);
                input.append('/');
                suppressNextChar = true;
                return true;
            } else if (keycode == OptionsManager.CHAT_KEY.intValue()) {
                active = true;
                input.setLength(0);
                suppressNextChar = true;
                return true;
            }
            return false;
        }

        if (keycode == Input.Keys.ESCAPE) {
            input.setLength(0);
            active = false;
        } else if (keycode == Input.Keys.ENTER) {
            if (!input.isEmpty()) {
                if (input.charAt(0) == '/') {
                    executeCommand(input.toString());
                } else {
                    Message message = new Message(game.getCharacterId(), input.toString());
                    messages.add(message);
                    game.getClient().sendTCP(new ChatMessageSendC2S(message));
                    MessageChatEvent.EVENT.invoker().onMessageChat(game, message);
                }
            }
            input.setLength(0);
            active = false;
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        if (!active)
            return false;

        if (suppressNextChar) {
            suppressNextChar = false;
            return true;
        }

        if (character == '\b' && !input.isEmpty()) {
            input.deleteCharAt(input.length() - 1);
        } else {
            input.append(character);
            return true;
        }

        return false;
    }
}
