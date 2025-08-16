package vault_work_station.utils;

import iskallia.vault.util.MiscUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class MiscUtilsAdditions {

    public static boolean canFullyMergeIntoSlot(IItemHandler handler, int slot, ItemStack stack) {
        if (stack.isEmpty()) {
            return true;
        }
        ItemStack existing = handler.getStackInSlot(slot);
        if (existing.isEmpty()) {
            return true;
        }
        if (!MiscUtils.canMerge(existing, stack)) {
            return false;
        }
        return existing.getMaxStackSize() >= existing.getCount() + stack.getCount();
    }
}
