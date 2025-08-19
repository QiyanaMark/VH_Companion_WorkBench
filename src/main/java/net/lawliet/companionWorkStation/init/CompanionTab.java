package net.lawliet.companionWorkStation.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.lawliet.companionWorkStation.Items.ModItems;


public class CompanionTab {
    public static final CreativeModeTab COMPANION_TAB = new CreativeModeTab("companion_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.COMPANION_ESSENCE.get());
        }
    };
}
