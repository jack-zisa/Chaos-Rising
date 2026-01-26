package dev.creoii.chaos.client.input;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import dev.creoii.chaos.OptionsManager;
import dev.creoii.chaos.client.ClientGame;
import dev.creoii.chaos.client.render.entity.data.CharacterEntityRenderData;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.item.AbilityItem;
import dev.creoii.chaos.item.WeaponItem;
import dev.creoii.chaos.network.c2s.AttackC2S;
import dev.creoii.chaos.network.c2s.CharacterMoveC2S;
import dev.creoii.chaos.network.c2s.UseItemC2S;
import dev.creoii.chaos.client.util.Inputtable;

public record CharacterController() implements Inputtable {
    @Override
    public void keyHeld(InputManager manager, int keycode) {
        ClientGame game = manager.getGame();
        if (game.getChatManager().isActive() || game.getCharacter() == null)
            return;

        if (game.getCharacter().canMove()) {
            float dx = 0f;
            float dy = 0f;

            if (keycode == OptionsManager.LEFT_KEY.intValue())
                dx -= 1;
            if (keycode == OptionsManager.RIGHT_KEY.intValue())
                dx += 1;
            if (keycode == OptionsManager.FORWARDS_KEY.intValue())
                dy += 1;
            if (keycode == OptionsManager.BACKWARDS_KEY.intValue())
                dy -= 1;

            if (dx != 0f || dy != 0f) {
                boolean axis = true;
                boolean positive;
                if (dy != 0f) {
                    axis = false;
                    positive = dy > 0f;
                } else positive = dx > 0f;

                CharacterEntityRenderData character = game.getCharacter();
                game.getClient().sendUDP(new CharacterMoveC2S(character.id, axis, positive));

                Vector2 newPos = new Vector2(character.x, character.y).add(new Vector2(dx, dy).scl(character.statContainer.speed().value() / 8f));

                character.xv = newPos.x - character.x;
                character.yv = newPos.y - character.y;
                character.x = newPos.x;
                character.y = newPos.y;
                character.renderX = newPos.x;
                character.renderY = newPos.y;
            }
        }

        if (keycode == OptionsManager.ABILITY_KEY.intValue()) {
            Slot abilitySlot = game.getCharacter().getAbilitySlot();
            if (abilitySlot.getStack().getItem() instanceof AbilityItem abilityItem) {
                game.getClient().sendTCP(new UseItemC2S(game.getCharacter().id, abilitySlot));
            }
        }
    }

    @Override
    public void touchHeld(InputManager manager, int screenX, int screenY, int pointer, int button) {
        ClientGame game = manager.getGame();
        if (game.getChatManager().isActive() || game.getCharacter() == null)
            return;

        Slot weaponSlot = game.getCharacter().getWeaponSlot();
        if (weaponSlot.getStack().getItem() instanceof WeaponItem weaponItem) {
            Vector3 mousePos = game.getInputManager().getMousePos();
            game.getClient().sendTCP(new AttackC2S(game.getCharacter().id, weaponSlot, mousePos.x - (Entity.COORDINATE_SCALE / 2f), mousePos.y - (Entity.COORDINATE_SCALE / 2f)));
        }
    }
}
