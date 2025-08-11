package vault_work_station.Item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;


public class CompanionTab {
    public static final CreativeModeTab COMPANION_TAB = new CreativeModeTab("companiontab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.COMPANION_ESSENCE.get());
        }
    };
}
