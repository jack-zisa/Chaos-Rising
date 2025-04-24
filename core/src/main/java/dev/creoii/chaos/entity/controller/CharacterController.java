package dev.creoii.chaos.entity.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.character.CharacterEntity;
import dev.creoii.chaos.entity.inventory.Slot;
import dev.creoii.chaos.item.AbilityItem;
import dev.creoii.chaos.item.WeaponItem;
import dev.creoii.chaos.util.provider.vecprovider.*;

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

        Vector2 direction = new Vector2(dx, dy).nor();

        if (entity instanceof CharacterEntity character)
            character.setPrevPos(entity.getPos());

        entity.getPos().add(direction.scl(entity.getStats().speed.value() * (Entity.COORDINATE_SCALE / 2f) * delta));

        if (--weaponCooldown <= 0 && Gdx.input.isTouched()) {
            Slot weaponSlot = entity.getInventory().getWeaponSlot();
            if (weaponSlot.hasItem() && weaponSlot.getStack().getItem() instanceof WeaponItem weaponItem) {
                weaponItem.getAttack().attack(new MousePosVecProvider(), new SourceVecProvider(), getEntity());
                weaponCooldown = Math.max(1, 150 / Math.max(1, entity.getStats().attackSpeed.value()));
            }
        }
        if (--abilityCooldown <= 0 && Gdx.input.isKeyPressed(entity.getGame().getOptionsManager().ABILITY_KEY.intValue())) {
            Slot abilitySlot = entity.getInventory().getAbilitySlot();
            if (abilitySlot.hasItem() && abilitySlot.getStack().getItem() instanceof AbilityItem abilityItem) {
                abilityItem.getAttack().attack(new MousePosVecProvider(), new SourceVecProvider(), getEntity());
                abilityCooldown = abilityItem.getCooldown();
            }
        }
    }
}
