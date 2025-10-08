package dev.creoii.chaos.input;

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
        if (manager.getGame().getCommandManager().isActive())
            return;

        float dx = 0f;
        float dy = 0f;

        if (keycode == manager.getGame().getOptionsManager().LEFT_KEY.intValue())
            dx -= 1;
        if (keycode == manager.getGame().getOptionsManager().RIGHT_KEY.intValue())
            dx += 1;
        if (keycode == manager.getGame().getOptionsManager().FORWARDS_KEY.intValue())
            dy += 1;
        if (keycode == manager.getGame().getOptionsManager().BACKWARDS_KEY.intValue())
            dy -= 1;

        if (dx != 0f || dy != 0f) {
            manager.getGame().getClient().sendTCP(new CharacterMoveC2S(character.uuid, dx, dy));
        }

        if (keycode == manager.getGame().getOptionsManager().ABILITY_KEY.intValue()) {
            SlotRenderData abilitySlot = character.getAbilitySlot();
            if (abilitySlot.stack != ItemStack.EMPTY && abilitySlot.stack.getItem() instanceof AbilityItem abilityItem) {
                manager.getGame().getClient().sendTCP(new UseItemC2S(character.uuid, new SlotEntry(2, 1, abilitySlot.stack)));
            }
        }
    }

    @Override
    public void touchHeld(InputManager manager, int screenX, int screenY, int pointer, int button) {
        if (manager.getGame().getCommandManager().isActive())
            return;

        SlotRenderData weaponSlot = character.getWeaponSlot();
        if (weaponSlot.stack != ItemStack.EMPTY && weaponSlot.stack.getItem() instanceof WeaponItem weaponItem) {
            manager.getGame().getClient().sendTCP(new UseItemC2S(character.uuid, new SlotEntry(2, 0, weaponSlot.stack)));
        }
    }
}
