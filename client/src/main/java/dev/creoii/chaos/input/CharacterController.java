package dev.creoii.chaos.input;

import dev.creoii.chaos.ClientGame;
import dev.creoii.chaos.inventory.SlotEntry;
import dev.creoii.chaos.item.AbilityItem;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.item.WeaponItem;
import dev.creoii.chaos.network.packet.c2s.CharacterMoveC2S;
import dev.creoii.chaos.network.packet.c2s.UseItemC2S;
import dev.creoii.chaos.render.entity.data.CharacterEntityRenderData;
import dev.creoii.chaos.render.entity.data.SlotRenderData;
import dev.creoii.chaos.util.Inputtable;

public record CharacterController(CharacterEntityRenderData character) implements Inputtable {
    @Override
    public void keyHeld(InputManager manager, int keycode) {
        ClientGame game = manager.getGame();
        if (game.getCommandManager().isActive())
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

                game.getClient().sendTCP(new CharacterMoveC2S(character.uuid, dx, dy));
            }
        }

        if (keycode == game.getOptionsManager().ABILITY_KEY.intValue()) {
            SlotRenderData abilitySlot = character.getAbilitySlot();
            if (abilitySlot.stack != ItemStack.EMPTY && abilitySlot.stack.getItem() instanceof AbilityItem abilityItem) {
                //game.getClient().sendTCP(new UseItemC2S(character.uuid, new SlotEntry(2, 1, Slot.Type.ABILITY, abilitySlot.stack, true)));
            }
        }
    }

    @Override
    public void touchHeld(InputManager manager, int screenX, int screenY, int pointer, int button) {
        if (manager.getGame().getCommandManager().isActive())
            return;

        SlotRenderData weaponSlot = character.getWeaponSlot();
        if (weaponSlot.stack != ItemStack.EMPTY && weaponSlot.stack.getItem() instanceof WeaponItem weaponItem) {
            manager.getGame().getClient().sendTCP(new UseItemC2S(character.uuid, new SlotEntry(2, 0, weaponSlot.type, weaponSlot.stack, true)));
        }
    }
}
