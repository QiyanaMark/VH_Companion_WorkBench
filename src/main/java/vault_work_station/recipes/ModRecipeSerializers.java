package vault_work_station.recipes;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vault_work_station.vault_work_station;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, vault_work_station.MOD_ID);

    public static final RegistryObject<RecipeSerializer<SmeltingRecipe>> SMELTING_SERIALIZER =
            SERIALIZERS.register("smelting", () -> SmeltingRecipe.Serializer.INSTANCE);
}