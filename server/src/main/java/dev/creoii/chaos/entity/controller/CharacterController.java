package dev.creoii.chaos.entity.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.ServerEntity;
import dev.creoii.chaos.entity.character.ServerCharacterEntity;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.item.AbilityItem;
import dev.creoii.chaos.item.WeaponItem;
import dev.creoii.chaos.network.packet.c2s.KeyInputC2S;
import dev.creoii.chaos.network.packet.c2s.MouseInputC2S;
import dev.creoii.chaos.util.provider.vecprovider.MousePosVecProvider;
import dev.creoii.chaos.util.provider.vecprovider.SourcePosVecProvider;

public class CharacterController extends EntityController<ServerCharacterEntity> {
    private int weaponCooldown;
    private int abilityCooldown;

    public CharacterController(ServerCharacterEntity character) {
        super(character);
        weaponCooldown = Math.max(1, 150 / Math.max(1, entity.getStats().attackSpeed.value()));
        abilityCooldown = 0;
    }

    @Override
    public void control(int gametime, float delta) {

    }

    public void onKey(KeyInputC2S.Action action, int keycode) {
        if (/*getEntity().getGame().getCommandManager().isActive() || */action == KeyInputC2S.Action.UP)
            return;

        float dx = 0f;
        float dy = 0f;

        if (keycode == entity.getGame().getOptionsManager().LEFT_KEY.intValue())
            dx -= 1;
        if (keycode == entity.getGame().getOptionsManager().RIGHT_KEY.intValue())
            dx += 1;
        if (keycode == entity.getGame().getOptionsManager().FORWARDS_KEY.intValue())
            dy += 1;
        if (keycode == entity.getGame().getOptionsManager().BACKWARDS_KEY.intValue())
            dy -= 1;

        Vector2 direction = new Vector2(dx, dy).nor();

        if (entity instanceof ServerCharacterEntity character)
            character.setPrevPos(entity.getPos());

        entity.getPos().add(direction.scl(entity.getStats().speed.value() * (ServerEntity.COORDINATE_SCALE / 2f)));

        if (--abilityCooldown <= 0 && keycode == entity.getGame().getOptionsManager().ABILITY_KEY.intValue()) {
            Slot abilitySlot = entity.getInventory().getAbilitySlot();
            if (abilitySlot.hasItem() && abilitySlot.getStack().getItem() instanceof AbilityItem abilityItem) {
                abilityItem.getAttack().attack(new MousePosVecProvider(), new SourcePosVecProvider(), getEntity());
                abilityCooldown = abilityItem.getCooldown();
            }
        }
    }

    public void onMouse(MouseInputC2S.Action action, int screenX, int screenY) {
        if (/*getEntity().getGame().getCommandManager().isActive() || */action != MouseInputC2S.Action.DOWN)
            return;

        if (--weaponCooldown <= 0 && Gdx.input.isTouched()) {
            Slot weaponSlot = entity.getInventory().getWeaponSlot();
            if (weaponSlot.hasItem() && weaponSlot.getStack().getItem() instanceof WeaponItem weaponItem) {
                weaponItem.getAttack().attack(new MousePosVecProvider(), new SourcePosVecProvider(), getEntity());
                weaponCooldown = Math.max(1, 150 / Math.max(1, entity.getStats().attackSpeed.value()));
            }
        }
    }
}
