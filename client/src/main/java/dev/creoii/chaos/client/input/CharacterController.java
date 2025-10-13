package dev.creoii.chaos.client.input;

import com.badlogic.gdx.math.Vector3;
import dev.creoii.chaos.client.ClientGame;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.item.AbilityItem;
import dev.creoii.chaos.item.WeaponItem;
import dev.creoii.chaos.network.c2s.AttackC2S;
import dev.creoii.chaos.network.c2s.CharacterMoveC2S;
import dev.creoii.chaos.network.c2s.UseItemC2S;
import dev.creoii.chaos.client.render.entity.data.CharacterEntityRenderData;
import dev.creoii.chaos.client.util.Inputtable;

public record CharacterController(CharacterEntityRenderData character) implements Inputtable {
    @Override
    public void keyHeld(InputManager manager, int keycode) {
        ClientGame game = manager.getGame();
        if (game.getChatManager().isActive())
            return;

        if (game.getCharacter().canMove()) {
            float dx = 0f;
            float dy = 0f;

            if (keycode == game.getOptionsManager().LEFT_KEY.intValue())
                dx -= 1;
            if (keycode == game.getOptionsManager().RIGHT_KEY.intValue())
                dx += 1;
            if (keycode == game.getOptionsManager().FORWARDS_KEY.intValue())
                dy += 1;
            if (keycode == game.getOptionsManager().BACKWARDS_KEY.intValue())
                dy -= 1;

            if (dx != 0f || dy != 0f) {
                float len = (float) Math.sqrt(dx * dx + dy * dy);
                dx /= len;
                dy /= len;

                game.getClient().sendTCP(new CharacterMoveC2S(character.id, dx, dy));
            }
        }

        if (keycode == game.getOptionsManager().ABILITY_KEY.intValue()) {
            Slot abilitySlot = character.getAbilitySlot();
            if (abilitySlot.getStack().getItem() instanceof AbilityItem abilityItem) {
                game.getClient().sendTCP(new UseItemC2S(character.id, abilitySlot));
            }
        }
    }

    @Override
    public void touchHeld(InputManager manager, int screenX, int screenY, int pointer, int button) {
        if (manager.getGame().getChatManager().isActive())
            return;

        Slot weaponSlot = character.getWeaponSlot();
        if (weaponSlot.getStack().getItem() instanceof WeaponItem weaponItem) {
            Vector3 mousePos = manager.getGame().getInputManager().getMousePos();
            manager.getGame().getClient().sendTCP(new AttackC2S(character.id, weaponSlot, mousePos.x - (Entity.COORDINATE_SCALE / 2f), mousePos.y - (Entity.COORDINATE_SCALE / 2f)));
        }
    }
}
