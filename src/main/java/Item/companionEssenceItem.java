package Item;

import net.minecraft.world.item.ItemStack;

import java.util.Properties;

public class companionEssenceItem extends Items {
    public COMPANIONESSENCE(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true; // This will make the item always have the foil effect
    }
}
