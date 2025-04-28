package dev.creoii.chaos.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.ClientGame;
import dev.creoii.chaos.entity.CharacterEntity;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.item.AbilityItem;
import dev.creoii.chaos.item.WeaponItem;
import dev.creoii.chaos.network.packet.c2s.CharacterStateC2S;
import dev.creoii.chaos.util.Inputtable;
import dev.creoii.chaos.util.provider.vecprovider.MousePosVecProvider;
import dev.creoii.chaos.util.provider.vecprovider.SourcePosVecProvider;

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
                Vector2 newPos = character.getPos().add(new Vector2(dx, dy).nor().scl(character.getStats().speed.value() / 8f));
                clientGame.getClient().sendTCP(new CharacterStateC2S(character.getUuid(), newPos.x, newPos.y));
            }

            if (keycode == character.getGame().getOptionsManager().ABILITY_KEY.intValue()) {
                if (--abilityCooldown <= 0) {
                    Slot abilitySlot = character.getInventory().getAbilitySlot();
                    if (abilitySlot.hasItem() && abilitySlot.getStack().getItem() instanceof AbilityItem abilityItem) {
                        abilityItem.getAttack().attack(new MousePosVecProvider(), new SourcePosVecProvider(), character);
                        abilityCooldown = abilityItem.getCooldown();
                    }
                }
            }
        }
    }

    @Override
    public boolean touchDown(InputManager manager, int screenX, int screenY, int pointer, int button) {
        if (character.getGame() instanceof ClientGame clientGame) {
            if (clientGame.getCommandManager().isActive())
                return false;

            if (--weaponCooldown <= 0 && Gdx.input.isTouched()) {
                Slot weaponSlot = character.getInventory().getWeaponSlot();
                if (weaponSlot.hasItem() && weaponSlot.getStack().getItem() instanceof WeaponItem weaponItem) {
                    weaponItem.getAttack().attack(new MousePosVecProvider(), new SourcePosVecProvider(), character);
                    weaponCooldown = Math.max(1, 150 / Math.max(1, character.getStats().attackSpeed.value()));
                }
            }
        }
        return false;
    }
}
