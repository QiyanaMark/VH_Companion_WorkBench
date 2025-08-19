package vault_work_station.compat;

import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;
import vault_work_station.VaultWorkStation;
import vault_work_station.Items.ModItems;
import vault_work_station.blocks.ModBlocks;

@SuppressWarnings("unused")
@JeiPlugin
public class JEIPlugin implements IModPlugin {

    private static final ResourceLocation PLUGIN_ID =
            new ResourceLocation(VaultWorkStation.MOD_ID, "jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        // Existing ingredient info
        registration.addIngredientInfo(
                new ItemStack(ModItems.COMPANION_FRAGMENT.get()),
                VanillaTypes.ITEM_STACK,
                new TranslatableComponent("jei.vault_work_station.particle_fragment.desc")
        );
        registration.addIngredientInfo(
                new ItemStack(ModItems.COMPANION_ESSENCE.get()),
                VanillaTypes.ITEM_STACK,
                new TranslatableComponent("jei.vault_work_station.companion_essence.desc")
        );

        // Register Companion Workbench recipes

    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.COMPANION_RECYCLER_BLOCK.get()),
                VanillaRecipeCategoryUid.CRAFTING
        );

    }
}
