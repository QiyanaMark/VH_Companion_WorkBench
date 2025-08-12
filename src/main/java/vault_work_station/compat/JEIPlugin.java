package vault_work_station.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import vault_work_station.Item.ModItems;
import vault_work_station.vault_work_station;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(vault_work_station.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        // Descriptions for your items
        registration.addIngredientInfo(
                ModItems.COMPANION_ESSENCE.getStack(),
                VanillaTypes.ITEM_STACK,
                "Used for crafting companion-related items"
        );

        registration.addIngredientInfo(
                ModItems.PARTICLE_FRAGMENT.getStack(),
                VanillaTypes.ITEM_STACK,
                "Fragment of mysterious particles"
        );

        registration.addIngredientInfo(
                ModItems.RELIC_FRAGMENT.getStack(),
                VanillaTypes.ITEM_STACK,
                "Ancient fragment with unknown powers"
        );
    }
}