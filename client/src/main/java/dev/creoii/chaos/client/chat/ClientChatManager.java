package dev.creoii.chaos.client.chat;

import com.badlogic.gdx.Input;
import dev.creoii.chaos.ChatManager;
import dev.creoii.chaos.OptionsManager;
import dev.creoii.chaos.chat.Message;
import dev.creoii.chaos.client.ClientWorld;
import dev.creoii.chaos.client.input.InputManager;
import dev.creoii.chaos.client.util.Inputtable;
import dev.creoii.chaos.network.c2s.ChatMessageSendC2S;
import dev.creoii.chaos.network.c2s.ExecuteCommandC2S;
import dev.creoii.chaos.util.event.ExecuteCommandEvent;
import dev.creoii.chaos.util.event.MessageChatEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientChatManager implements ChatManager, Inputtable {
    private final ClientWorld world;
    private final List<Message> messages;
    private final StringBuilder input = new StringBuilder();
    private boolean active = false;
    private boolean suppressNextChar = false;

    public ClientChatManager(ClientWorld world) {
        this.world = world;
        messages = new ArrayList<>();
    }

    @Override
    public ClientWorld world() {
        return world;
    }

    @Override
    public List<Message> messages() {
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
        ExecuteCommandEvent.EVENT.invoker().onExecuteCommand(world, world.getGame().getCharacterId(), command, args);
        world.getGame().getClient().sendTCP(new ExecuteCommandC2S(world.getGame().getCharacter().id, commandType, args));
    }

    @Override
    public boolean keyDown(InputManager manager, int keycode) {
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
                    Message message = new Message(world.getGame().getCharacterId(), input.toString());
                    world.getGame().getClient().sendTCP(new ChatMessageSendC2S(message));
                    MessageChatEvent.EVENT.invoker().onMessageChat(world, message);
                }
            }
            input.setLength(0);
            active = false;
        }

        return true;
    }

    @Override
    public boolean keyTyped(InputManager manager, char character) {
        if (!active)
            return false;

        if (suppressNextChar) {
            suppressNextChar = false;
            return true;
        }

        if (character == '\b') {
            if (!input.isEmpty())
                input.deleteCharAt(input.length() - 1);
        } else {
            input.append(character);
            return true;
        }

        return false;
    }
}
