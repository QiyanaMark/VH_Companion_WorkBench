package vault_work_station.mixin.enumExtension;

import iskallia.vault.config.recipe.ForgeRecipeType;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import vault_work_station.config.ModConfigs;
import vault_work_station.recipe.companionWorkBench.CompanionWorkBenchRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Mixin(value = ForgeRecipeType.class, remap = false)
public class ForgeRecipeTypeEnumMixin {
    @Final
    @Mutable
    @Shadow
    private static ForgeRecipeType[] $VALUES;

    @Unique
    private static final ForgeRecipeType COMPANION_WORKBENCH = enumExpansion$addVariant("COMPANION_WORKBENCH", CompanionWorkBenchRecipe::new, () -> ModConfigs.COMPANION_WORKBENCH_RECIPES);


    @Invoker("<init>")
    public static ForgeRecipeType enumExpansion$invokeInit(String internalName, int internalId, BiFunction recipeClassCtor, Supplier configSupplier) {
        throw  new AssertionError();
    }

    @Unique
    private static ForgeRecipeType enumExpansion$addVariant(String internalName, BiFunction recipeClassCtor, Supplier configSupplier) {
        ArrayList<ForgeRecipeType> variants = new ArrayList<>(Arrays.asList(ForgeRecipeTypeEnumMixin.$VALUES));
        ForgeRecipeType type = enumExpansion$invokeInit(internalName, variants.get(variants.size() - 1).ordinal() + 1, recipeClassCtor, configSupplier);
        variants.add(type);
        ForgeRecipeTypeEnumMixin.$VALUES = variants.toArray(new ForgeRecipeType[0]);
        return type;
    }
}
