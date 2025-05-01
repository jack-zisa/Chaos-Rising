package dev.creoii.chaos.input;

import com.badlogic.gdx.Gdx;
import dev.creoii.chaos.ClientGame;
import dev.creoii.chaos.entity.CharacterEntity;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.inventory.SlotEntry;
import dev.creoii.chaos.item.AbilityItem;
import dev.creoii.chaos.item.WeaponItem;
import dev.creoii.chaos.network.packet.c2s.CharacterMoveC2S;
import dev.creoii.chaos.network.packet.c2s.UseItemC2S;
import dev.creoii.chaos.util.Inputtable;

public class CharacterController implements Inputtable {
    private final CharacterEntity character;
    private int weaponCooldown;
    private int abilityCooldown;

    public CharacterController(CharacterEntity character) {
        this.character = character;
        weaponCooldown = Math.max(1, 150 / Math.max(1, character.getStats().attackSpeed.value()));
        abilityCooldown = 0;
    }

    @Override
    public void keyHeld(InputManager manager, int keycode) {
        if (character.getGame() instanceof ClientGame clientGame) {
            if (clientGame.getCommandManager().isActive())
                return;

            float dx = 0f;
            float dy = 0f;

            if (keycode == clientGame.getOptionsManager().LEFT_KEY.intValue())
                dx -= 1;
            if (keycode == clientGame.getOptionsManager().RIGHT_KEY.intValue())
                dx += 1;
            if (keycode == clientGame.getOptionsManager().FORWARDS_KEY.intValue())
                dy += 1;
            if (keycode == clientGame.getOptionsManager().BACKWARDS_KEY.intValue())
                dy -= 1;

            if (dx != 0f || dy != 0f) {
                clientGame.getClient().sendTCP(new CharacterMoveC2S(character.getUuid(), dx, dy));
            }

            if (keycode == character.getGame().getOptionsManager().ABILITY_KEY.intValue()) {
                if (--abilityCooldown <= 0) {
                    Slot abilitySlot = character.getInventory().getAbilitySlot();
                    if (abilitySlot.hasItem() && abilitySlot.getStack().getItem() instanceof AbilityItem abilityItem) {
                        clientGame.getClient().sendTCP(new UseItemC2S(character.getUuid(), new SlotEntry(abilitySlot.getR(), abilitySlot.getC(), abilitySlot.hasItem() ? abilitySlot.getStack().getItem().id() : "", abilitySlot.hasItem() ? abilitySlot.getStack().getCount() : 0)));
                        abilityCooldown = abilityItem.getCooldown();
                    }
                }
            }
        }
    }

    @Override
    public void touchHeld(InputManager manager, int screenX, int screenY, int pointer, int button) {
        if (character.getGame() instanceof ClientGame clientGame) {
            if (clientGame.getCommandManager().isActive())
                return;

            if (--weaponCooldown <= 0) {
                Slot weaponSlot = character.getInventory().getWeaponSlot();
                if (weaponSlot.hasItem() && weaponSlot.getStack().getItem() instanceof WeaponItem weaponItem) {
                    clientGame.getClient().sendTCP(new UseItemC2S(character.getUuid(), new SlotEntry(weaponSlot.getR(), weaponSlot.getC(), weaponSlot.hasItem() ? weaponSlot.getStack().getItem().id() : "", weaponSlot.hasItem() ? weaponSlot.getStack().getCount() : 0)));
                    weaponCooldown = Math.max(1, 150 / Math.max(1, character.getStats().attackSpeed.value()));
                }
            }
        }
    }
}
