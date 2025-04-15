package dev.creoii.chaos.entity.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.render.screen.InventoryScreen;

public class Slot {
    private final int x;
    private final int y;
    private final Type type;
    private ItemStack stack;

    public Slot(int x, int y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public Slot(int x, int y) {
        this(x, y, Type.NONE);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Type getType() {
        return type;
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    public boolean hasItem() {
        return stack != null && stack.getItem() != null;
    }

    public Slot copy() {
        Slot slot = new Slot(x, y);
        slot.setStack(stack);
        return slot;
    }

    public enum Type {
        NONE("textures/ui/slot.png"),
        WEAPON("textures/ui/weapon_slot.png"),
        ABILITY("textures/ui/ability_slot.png"),
        ARMOR("textures/ui/armor_slot.png"),
        ACCESSORY("textures/ui/accessory_slot.png");

        private final Sprite sprite;

        Type(String textureId) {
            sprite = new Sprite(new Texture(textureId));
            sprite.setSize(InventoryScreen.SLOT_SIZE, InventoryScreen.SLOT_SIZE);
        }

        public Sprite getSprite() {
            return sprite;
        }
    }
}
