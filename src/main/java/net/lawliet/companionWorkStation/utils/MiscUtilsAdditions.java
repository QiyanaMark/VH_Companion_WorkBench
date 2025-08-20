package net.lawliet.companionWorkStation.utils;

import iskallia.vault.util.MiscUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

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

    // Make sure to check whether item can be inserted into slot beforehand
    public static ItemStack insertIntoSlot(ItemStackHandler handler, int slot, ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack existing = handler.getStackInSlot(slot);
        if (existing.isEmpty()) {
            handler.setStackInSlot(slot, stack);
            return ItemStack.EMPTY;
        }
        if (!MiscUtils.canMerge(existing, stack)) {
            return stack;
        }
        ItemStack newStack = existing.copy();
        newStack.grow(stack.getCount());
        handler.setStackInSlot(slot, newStack);
        return ItemStack.EMPTY;
    }
}
