package net.lawliet.companionWorkStation.recipe.companionWorkBench;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.recipe.ForgeRecipeType;
import iskallia.vault.config.recipe.ForgeRecipesConfig;
import net.minecraft.world.item.ItemStack;
import net.lawliet.companionWorkStation.Items.ModItems;
import net.lawliet.companionWorkStation.CompanionWorkStation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CompanionWorkBenchRecipesConfig extends ForgeRecipesConfig<ConfigCompanionWorkBenchRecipe, CompanionWorkBenchRecipe> {
    @Expose
    private final List<ConfigCompanionWorkBenchRecipe> companionWorkBenchRecipes = new ArrayList<>();

    public CompanionWorkBenchRecipesConfig() {
        super(ForgeRecipeType.valueOf("COMPANION_WORKBENCH"));
        this.root = "config%s%s%s".formatted(File.separator, CompanionWorkStation.MOD_ID, File.separator);
    }

    @Override
    public List<ConfigCompanionWorkBenchRecipe> getConfigRecipes() {
        return this.companionWorkBenchRecipes;
    }

    @Override
    protected void reset() {
        companionWorkBenchRecipes.clear();
        ConfigCompanionWorkBenchRecipe companionEgg = new ConfigCompanionWorkBenchRecipe(new ItemStack(iskallia.vault.init.ModItems.COMPANION_EGG));
        companionEgg.addInput(new ItemStack(ModItems.COMPANION_ESSENCE.get(), 4));
        companionEgg.addInput(new ItemStack(iskallia.vault.init.ModItems.EXTRAORDINARY_BENITOITE, 4));
        companionEgg.addInput(new ItemStack(iskallia.vault.init.ModItems.POG, 4));
        companionWorkBenchRecipes.add(companionEgg);

        ConfigCompanionWorkBenchRecipe companionRelic = new ConfigCompanionWorkBenchRecipe(new ItemStack(iskallia.vault.init.ModItems.COMPANION_RELIC));
        companionRelic.addInput(new ItemStack(ModItems.COMPANION_FRAGMENT.get(),12));
        companionRelic.addInput(new ItemStack(iskallia.vault.init.ModItems.VAULT_CATALYST_FRAGMENT, 18));
        companionWorkBenchRecipes.add(companionRelic);

        ConfigCompanionWorkBenchRecipe companionParticleTrail = new ConfigCompanionWorkBenchRecipe(new ItemStack(iskallia.vault.init.ModItems.COMPANION_PARTICLE_TRAIL));
        companionParticleTrail.addInput(new ItemStack(ModItems.COMPANION_FRAGMENT.get(), 4));
        companionParticleTrail.addInput(new ItemStack(iskallia.vault.init.ModItems.EXTRAORDINARY_LARIMAR));
        companionWorkBenchRecipes.add(companionParticleTrail);
    }
}
