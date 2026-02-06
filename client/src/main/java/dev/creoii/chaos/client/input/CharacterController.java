package dev.creoii.chaos.client.input;

import com.badlogic.gdx.math.Vector3;
import dev.creoii.chaos.client.option.OptionsManager;
import dev.creoii.chaos.client.ClientGame;
import dev.creoii.chaos.client.render.entity.data.CharacterEntityRenderData;
import dev.creoii.chaos.client.render.screen.InventoryScreen;
import dev.creoii.chaos.client.render.screen.Screen;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.item.AbilityItem;
import dev.creoii.chaos.item.WeaponItem;
import dev.creoii.chaos.network.c2s.*;
import dev.creoii.chaos.client.util.Inputtable;

public record CharacterController() implements Inputtable {
    @Override
    public boolean keyDown(InputManager manager, int keycode) {
        ClientGame game = manager.getGame();
        CharacterEntityRenderData character = game.getCharacter();

        if (game.getWorld().getChatManager().isActive() || character == null)
            return false;

        if (character.canMove()) {
            float dx = 0f;
            float dy = 0f;

            if (keycode == OptionsManager.LEFT_KEY.value())
                dx -= 1;
            if (keycode == OptionsManager.RIGHT_KEY.value())
                dx += 1;
            if (keycode == OptionsManager.UP_KEY.value())
                dy += 1;
            if (keycode == OptionsManager.DOWN_KEY.value())
                dy -= 1;

            if (dx != 0f || dy != 0f) {
                boolean axis = true;
                boolean positive;
                if (dy != 0f) {
                    axis = false;
                    positive = dy > 0f;
                } else positive = dx > 0f;

                game.getClient().sendUDP(new CharacterMoveStartC2S(character.id, axis, positive));
                return true;
            }
        }

        return false;
    }

    @Override
    public void keyHeld(InputManager manager, int keycode) {
        ClientGame game = manager.getGame();
        CharacterEntityRenderData character = game.getCharacter();

        if (game.getWorld().getChatManager().isActive() || character == null)
            return;

        if (keycode == OptionsManager.ABILITY_KEY.value()) {
            Slot abilitySlot = character.getAbilitySlot();
            if (abilitySlot.getStack().getItem() instanceof AbilityItem abilityItem) {
                game.getClient().sendTCP(new UseItemC2S(character.id, abilitySlot));
            }
        }
    }

    @Override
    public boolean keyUp(InputManager manager, int keycode) {
        ClientGame game = manager.getGame();
        CharacterEntityRenderData character = game.getCharacter();

        if (character == null)
            return false;

        if (character.isMoving()) {
            if (game.getWorld().getChatManager().isActive()) {
                character.stopMoving();
                game.getClient().sendUDP(new CharacterStopMoveC2S(character.id));
                return true;
            } else if (OptionsManager.isMovementKey(keycode)) {
                boolean axis;
                boolean positive;

                if (keycode == OptionsManager.LEFT_KEY.value()) {
                    axis = true;
                    positive = false;
                } else if (keycode == OptionsManager.RIGHT_KEY.value()) {
                    axis = true;
                    positive = true;
                } else if (keycode == OptionsManager.UP_KEY.value()) {
                    axis = false;
                    positive = true;
                } else if (keycode == OptionsManager.DOWN_KEY.value()) {
                    axis = false;
                    positive = false;
                } else return false;

                game.getClient().sendUDP(new CharacterMoveEndC2S(character.id, axis, positive));
                return true;
            }
        }
        return false;
    }

    @Override
    public void touchHeld(InputManager manager, int screenX, int screenY, int pointer, int button) {
        ClientGame game = manager.getGame();
        CharacterEntityRenderData character = game.getCharacter();
        if (game.getWorld().getChatManager().isActive() || character == null)
            return;

        Slot weaponSlot = character.getWeaponSlot();
        if (weaponSlot.getStack().getItem() instanceof WeaponItem && character.canAttack(game) && character.statContainer.attackSpeed().value() > 0f) {
            Screen screen = game.getRenderer().getCurrentScreen();
            if (screen instanceof InventoryScreen inventoryScreen && inventoryScreen.getMouseOverSlot() != null)
                return;

            Vector3 mousePos = game.getInputManager().getMousePos();
            game.getClient().sendTCP(new AttackC2S(character.id, weaponSlot, mousePos.x - (Entity.COORDINATE_SCALE / 2f), mousePos.y - (Entity.COORDINATE_SCALE / 2f)));

            character.setLastAttackTime(System.currentTimeMillis());
        }
    }
}
