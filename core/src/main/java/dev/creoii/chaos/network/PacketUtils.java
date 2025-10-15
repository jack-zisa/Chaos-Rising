package dev.creoii.chaos.network;

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.chat.Message;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.item.Item;
import dev.creoii.chaos.item.ItemStack;

public final class PacketUtils {
    public static Item readItem(Input input) {
        String id = input.readString();
        return DataManager.getItem(id);
    }

    public static void writeItem(Output output, Item item) {
        output.writeString(item != null ? item.id() : "");
    }

    public static ItemStack readItemStack(Input input) {
        Item item = readItem(input);
        int count = input.readInt();
        return new ItemStack(item, count);
    }

    public static void writeItemStack(Output output, ItemStack stack) {
        writeItem(output, stack.getItem());
        output.writeInt(stack.getCount());
    }

    public static Message readMessage(Input input) {
        return new Message(input.readInt(), input.readString(), readColor(input));
    }

    public static void writeMessage(Output output, Message message) {
        output.writeInt(message.getSenderId());
        output.writeString(message.getText());
        writeColor(output, message.getColor());
    }

    public static Color readColor(Input input) {
        return Color.valueOf(input.readString());
    }

    public static void writeColor(Output output, Color color) {
        output.writeString(color.toString());
    }

    public static <E extends Enum<E>> E readEnum(Class<E> enumClass, Input input) {
        int ordinal = input.readVarInt(true);
        E[] constants = enumClass.getEnumConstants();
        if (ordinal < 0 || ordinal >= constants.length)
            throw new IllegalArgumentException("Invalid ordinal " + ordinal + " for enum " + enumClass.getSimpleName());
        return constants[ordinal];
    }

    public static <E extends Enum<E>> void writeEnum(Output output, E e) {
        output.writeVarInt(e.ordinal(), true);
    }
    public static Slot readSlot(Input input) {
        return new Slot(input.readInt(), input.readInt(), readEnum(Slot.Type.class, input), readItemStack(input));
    }

    public static void writeSlot(Output output, Slot slot) {
        output.writeInt(slot.getR());
        output.writeInt(slot.getC());
        writeEnum(output, slot.getType());
        writeItemStack(output, slot.getStack());
    }
}
