package dev.creoii.chaos.client.input;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import dev.creoii.chaos.OptionsManager;
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

        if (game.getChatManager().isActive() || character == null)
            return false;

        if (character.canMove()) {
            float dx = 0f;
            float dy = 0f;

            if (keycode == OptionsManager.LEFT_KEY.intValue())
                dx -= 1;
            if (keycode == OptionsManager.RIGHT_KEY.intValue())
                dx += 1;
            if (keycode == OptionsManager.UP_KEY.intValue())
                dy += 1;
            if (keycode == OptionsManager.DOWN_KEY.intValue())
                dy -= 1;

            if (dx != 0f || dy != 0f) {
                boolean axis = true;
                boolean positive;
                if (dy != 0f) {
                    axis = false;
                    positive = dy > 0f;
                } else positive = dx > 0f;

                game.getClient().sendUDP(new CharacterMoveStartC2S(character.id, axis, positive));

                Vector2 newPos = new Vector2(character.x, character.y).add(new Vector2(dx, dy).scl(character.statContainer.speed().value() / 8f));

                character.xv = newPos.x - character.x;
                character.yv = newPos.y - character.y;
                character.x = newPos.x;
                character.y = newPos.y;
                character.renderX = newPos.x;
                character.renderY = newPos.y;
                return true;
            }
        }

        return false;
    }

    @Override
    public void keyHeld(InputManager manager, int keycode) {
        ClientGame game = manager.getGame();
        CharacterEntityRenderData character = game.getCharacter();

        if (game.getChatManager().isActive() || character == null)
            return;

        if (keycode == OptionsManager.ABILITY_KEY.intValue()) {
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

        if (game.getChatManager().isActive()) {
            game.getClient().sendUDP(new CharacterStopMoveC2S(character.id));
            return false;
        }

        boolean axis;
        boolean positive;

        if (keycode == OptionsManager.LEFT_KEY.intValue()) {
            axis = true;
            positive = false;
        } else if (keycode == OptionsManager.RIGHT_KEY.intValue()) {
            axis = true;
            positive = true;
        } else if (keycode == OptionsManager.UP_KEY.intValue()) {
            axis = false;
            positive = true;
        } else if (keycode == OptionsManager.DOWN_KEY.intValue()) {
            axis = false;
            positive = false;
        } else return false;

        game.getClient().sendUDP(new CharacterMoveEndC2S(character.id, axis, positive));
        return true;
    }

    @Override
    public void touchHeld(InputManager manager, int screenX, int screenY, int pointer, int button) {
        ClientGame game = manager.getGame();
        if (game.getChatManager().isActive() || game.getCharacter() == null)
            return;

        Slot weaponSlot = game.getCharacter().getWeaponSlot();
        if (weaponSlot.getStack().getItem() instanceof WeaponItem weaponItem && game.getAttackCooldown() <= 0) {
            Screen screen = game.getRenderer().getCurrentScreen();
            if (screen instanceof InventoryScreen inventoryScreen && inventoryScreen.getMouseOverSlot() != null)
                return;

            float attackSpeed = game.getCharacter().statContainer.attackSpeed().value();
            if (attackSpeed <= 0f)
                return;

            float attacks = 1.5f + 6.5f * (attackSpeed / 75f);
            attacks *= weaponItem.getRateOfFire();

            game.setAttackCooldown(1f / attacks);

            Vector3 mousePos = game.getInputManager().getMousePos();
            game.getClient().sendTCP(new AttackC2S(game.getCharacter().id, weaponSlot, mousePos.x - (Entity.COORDINATE_SCALE / 2f), mousePos.y - (Entity.COORDINATE_SCALE / 2f)));
        }
    }
}
