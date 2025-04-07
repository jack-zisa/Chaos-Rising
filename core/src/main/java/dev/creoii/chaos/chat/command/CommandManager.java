package dev.creoii.chaos.chat.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import dev.creoii.chaos.Main;

import java.util.Arrays;

public class CommandManager extends InputAdapter {
    private final Main main;
    private final StringBuilder command = new StringBuilder("/");
    private boolean active = false;

    public CommandManager(Main main) {
        this.main = main;
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

        if (Commands.ALL.containsKey(commandType)) {
            String[] args = Arrays.copyOfRange(elements, 1, elements.length);
            Commands.ALL.get(commandType).execute(main.getGame(), args);
            Gdx.app.log("CommandManager", "Executed " + commandType + " with args " + Arrays.toString(args));
        } else {
            Gdx.app.log("CommandManager", "Command " + commandType + " not found");
        }
    }

    public void update(int gametime) {
        if (Gdx.input.isKeyPressed(main.getGame().getInputManager().getKeycode("command"))) {
            active = true;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!active) return false;

        if (keycode == Input.Keys.ESCAPE) {
            active = false;
            command.setLength(1);
        } else if (keycode == Input.Keys.BACKSPACE && command.length() > 1) {
            command.deleteCharAt(command.length() - 1);
        } else if (keycode == Input.Keys.ENTER) {
            execute(command.toString());
            command.setLength(1);
            active = false;
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        if (!active) return false;

        if (Character.isLetterOrDigit(character) || character == ' ' || character == '_' || character == '.' || character == '-') {
            command.append(character);
            return true;
        }

        return false;
    }
}
