package dev.creoii.chaos.entity.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import dev.creoii.chaos.item.Item;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.render.screen.widget.InventoryWidget;

import java.util.function.Predicate;

public class Slot {
    private final int r;
    private final int c;
    private Type type;
    private ItemStack stack;

    public Slot(int r, int c, Type type) {
        this.r = r;
        this.c = c;
        this.type = type;
    }

    public Slot(int r, int c) {
        this(r, c, Type.NONE);
    }

    public int getR() {
        return r;
    }

    public int getC() {
        return c;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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

    public boolean canAccept(Item item) {
        return type.itemPredicate.test(item);
    }

    public Slot copy() {
        Slot slot = new Slot(r, c);
        slot.setStack(stack);
        return slot;
    }

    public enum Type {
        NONE("textures/ui/slot.png", item -> true),
        WEAPON("textures/ui/weapon_slot.png", item -> item.getType() == Item.Type.WEAPON),
        ABILITY("textures/ui/ability_slot.png", item -> item.getType() == Item.Type.ABILITY),
        ARMOR("textures/ui/armor_slot.png", item -> item.getType() == Item.Type.ARMOR),
        ACCESSORY("textures/ui/accessory_slot.png", item -> item.getType() == Item.Type.ACCESSORY);

        private final Sprite sprite;
        private final Predicate<Item> itemPredicate;

        Type(String textureId, Predicate<Item> itemPredicate) {
            this.itemPredicate = itemPredicate;
            sprite = new Sprite(new Texture(textureId));
            sprite.setSize(InventoryWidget.SLOT_SIZE, InventoryWidget.SLOT_SIZE);
        }

        public Sprite getSprite() {
            return sprite;
        }

        public Predicate<Item> getItemPredicate() {
            return itemPredicate;
        }
    }
}
