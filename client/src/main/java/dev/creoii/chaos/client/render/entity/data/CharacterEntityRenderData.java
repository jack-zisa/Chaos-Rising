package dev.creoii.chaos.client.render.entity.data;

import dev.creoii.chaos.client.ClientGame;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.item.WeaponItem;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.stat.StatContainer;

public class CharacterEntityRenderData extends LivingEntityRenderData {
    public Slot[][] slots;
    public int experience;
    public int level;
    private long lastAttackTime;

    public CharacterEntityRenderData(int id, float x, float y, float xv, float yv, String textureId, float scale, StatContainer statContainer, StatContainer maxStatContainer, Slot[][] slots) {
        super(id, EntityGroup.CHARACTER, x, y, xv, yv, textureId, scale, statContainer, maxStatContainer);
        this.slots = slots;
        experience = 0;
        level = 0;
        lastAttackTime = 0L;
    }

    public Slot[] getHotbar() {
        return slots[slots.length - 1];
    }

    public Slot getWeaponSlot() {
        return getHotbar()[0];
    }

    public Slot getAbilitySlot() {
        return getHotbar()[1];
    }

    public Slot getArmorSlot() {
        return getHotbar()[2];
    }

    public Slot getAccessorySlot() {
        return getHotbar()[3];
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);

        if (xv > .01f) {
            facingRight = true;
        } else if (xv < -.01f) {
            facingRight = false;
        }
    }

    public float getAttackSpeed() {
        return statContainer.attackSpeed().value();
    }

    public long getLastAttackTime() {
        return lastAttackTime;
    }

    public void setLastAttackTime(long attackTime) {
        lastAttackTime = attackTime;
    }

    public boolean canAttack(ClientGame game) {
        float attackSpeed = getAttackSpeed();
        if (attackSpeed <= 0f)
            return false;
        return System.currentTimeMillis() - getLastAttackTime() >= game.getAttacks();
    }
}
