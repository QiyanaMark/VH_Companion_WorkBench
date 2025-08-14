package vault_work_station.recipe;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vault_work_station.vault_work_station;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, vault_work_station.MOD_ID);

    public static final RegistryObject<RecipeType<CompanionWorkBenchRecipe>> COMPANION_RECIPE_TYPE =
            RECIPE_TYPES.register("companion_workbench", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "vault_work_station:companion_workbench";
                }
            });

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
    }
}