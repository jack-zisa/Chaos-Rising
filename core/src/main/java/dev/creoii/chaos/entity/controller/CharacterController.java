package dev.creoii.chaos.entity.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.attack.Attack;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.character.CharacterEntity;
import dev.creoii.chaos.entity.inventory.Slot;
import dev.creoii.chaos.item.AbilityItem;
import dev.creoii.chaos.item.WeaponItem;

public class CharacterController extends EntityController<CharacterEntity> {
    private int weaponCooldown;
    private int abilityCooldown;

    public CharacterController(CharacterEntity character) {
        super(character);
        weaponCooldown = Math.max(1, 150 / Math.max(1, entity.getStats().attackSpeed.value()));
        abilityCooldown = 0;
    }

    @Override
    public void control(int gametime, float delta) {
        if (getEntity().getGame().getCommandManager().isActive())
            return;

        float dx = 0f;
        float dy = 0f;

        if (Gdx.input.isKeyPressed(entity.getGame().getOptionsManager().LEFT_KEY.intValue()))
            dx -= 1;
        if (Gdx.input.isKeyPressed(entity.getGame().getOptionsManager().RIGHT_KEY.intValue()))
            dx += 1;
        if (Gdx.input.isKeyPressed(entity.getGame().getOptionsManager().FORWARDS_KEY.intValue()))
            dy += 1;
        if (Gdx.input.isKeyPressed(entity.getGame().getOptionsManager().BACKWARDS_KEY.intValue()))
            dy -= 1;

        if (dx == 0 && dy == 0) {
            entity.setMoving(false);
        }

        if (Gdx.input.isTouched()) {
            Vector2 direction = new Vector2(dx, dy).nor();

            if (entity instanceof CharacterEntity character)
                character.setPrevPos(entity.getPos());

            entity.getPos().add(direction.scl(entity.getStats().speed.value() * Entity.COORDINATE_SCALE * delta));
            entity.setMoving(true);

            if (Gdx.input.isTouched() && --weaponCooldown <= 0) {
                Slot weaponSlot = entity.getInventory().getWeaponSlot();
                if (weaponSlot.hasItem() && weaponSlot.getStack().getItem() instanceof WeaponItem weaponItem) {
                    weaponItem.getAttack().attack(Attack.Target.MOUSE_POS, entity);
                    weaponCooldown = Math.max(1, 150 / Math.max(1, entity.getStats().attackSpeed.value()));
                }
            }
        } else if (Gdx.input.isKeyPressed(entity.getGame().getOptionsManager().ABILITY_KEY.intValue())) {
            if (--abilityCooldown <= 0) {
                Slot abilitySlot = entity.getInventory().getAbilitySlot();
                if (abilitySlot.hasItem() && abilitySlot.getStack().getItem() instanceof AbilityItem abilityItem) {
                    abilityItem.getAttack().attack(abilityItem.getTarget(), getEntity());
                    abilityCooldown = abilityItem.getCooldown();
                }
            }
        }
    }
}
