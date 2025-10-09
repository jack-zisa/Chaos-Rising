package dev.creoii.chaos.render.data;

import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.inventory.SlotEntry;
import dev.creoii.chaos.item.ItemStack;

import java.util.List;

public class SlotRenderData {
    public Slot.Type type;
    public ItemStack stack;

    public SlotRenderData(Slot.Type type, ItemStack stack) {
        this.type = type;
        this.stack = stack;
    }

    public static SlotRenderData fromSlotEntry(SlotEntry slotEntry) {
        return new SlotRenderData(slotEntry.type(), slotEntry.stack());
    }

    public static SlotRenderData[][] fromSlotEntryList(List<List<SlotEntry>> list) {
        int r = list.size();
        int c = r > 0 ? list.getFirst().size() : 0;

        SlotRenderData[][] array = new SlotRenderData[r][c];

        for (int i = 0; i < r; ++i) {
            for (int j = 0; j < list.get(i).size(); ++j) {
                array[i][j] = fromSlotEntry(list.get(i).get(j));
            }
        }

        return array;
    }
}
