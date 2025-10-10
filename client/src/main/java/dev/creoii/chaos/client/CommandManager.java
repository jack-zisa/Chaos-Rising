package dev.creoii.chaos.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import dev.creoii.chaos.network.c2s.ExecuteCommandC2S;

import java.util.Arrays;

public class CommandManager extends InputAdapter {
    private final ClientGame game;
    private final StringBuilder command = new StringBuilder("/");
    private boolean active = false;

    public CommandManager(ClientGame game) {
        this.game = game;
    }

    public StringBuilder getCommand() {
        return command;
    }

    public boolean isActive() {
        return active;
    }

    public void execute(String command) {
        String[] elements = command.split(" ");
        String commandType = elements[0].substring(1);

        if (commandType.isEmpty())
            return;

        String[] args = Arrays.copyOfRange(elements, 1, elements.length);
        game.getClient().sendTCP(new ExecuteCommandC2S(game.getCharacter().id, commandType, args));

        /**
         * FOR /set_class:
         *
         *         sprite = new Sprite(game.getTextureManager().getTexture("class", getTextureId()));
         *         sprite.setSize(getScale(), getScale());
         */
    }

    public void update() {
        if (Gdx.input.isKeyPressed(game.getOptionsManager().COMMAND_KEY.intValue())) {
            active = true;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!active)
            return false;

        if (keycode == Input.Keys.ESCAPE) {
            active = false;
            command.setLength(1);
        } else if (keycode == Input.Keys.ENTER) {
            execute(command.toString());
            command.setLength(1);
            active = false;
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        if (!active)
            return false;

        if (Character.isLetterOrDigit(character) || character == ' ' || character == '_' || character == '.' || character == '-') {
            command.append(character);
            return true;
        } else if (character == '\b' && command.length() > 1) {
            command.deleteCharAt(command.length() - 1);
        }

        return false;
    }
}
