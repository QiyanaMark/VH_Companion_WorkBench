package vault_work_station.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vault_work_station.vault_work_station;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, vault_work_station.MOD_ID);

    public static final RegistryObject<RecipeType<SmeltingRecipe>> SMELTING =
            RECIPE_TYPES.register("smelting", () -> new RecipeType<>() {
                public String toString() {
                    return new ResourceLocation(vault_work_station.MOD_ID, "smelting").toString();
                }
            });
}