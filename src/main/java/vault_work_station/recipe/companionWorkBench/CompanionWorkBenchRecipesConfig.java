package vault_work_station.recipe.companionWorkBench;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.recipe.ForgeRecipeType;
import iskallia.vault.config.recipe.ForgeRecipesConfig;
import iskallia.vault.core.random.JavaRandom;
import iskallia.vault.item.CompanionEggItem;
import iskallia.vault.item.CompanionSeries;
import net.minecraft.world.item.ItemStack;
import vault_work_station.Items.ModItems;
import vault_work_station.VaultWorkStation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CompanionWorkBenchRecipesConfig extends ForgeRecipesConfig<ConfigCompanionWorkBenchRecipe, CompanionWorkBenchRecipe> {
    @Expose
    private final List<ConfigCompanionWorkBenchRecipe> companionWorkBenchRecipes = new ArrayList<>();

    public CompanionWorkBenchRecipesConfig() {
        super(ForgeRecipeType.valueOf("COMPANION_WORKBENCH"));
        this.root = "config%s%s%s".formatted(File.separator, VaultWorkStation.MOD_ID, File.separator);
    }

    @Override
    public List<ConfigCompanionWorkBenchRecipe> getConfigRecipes() {
        return this.companionWorkBenchRecipes;
    }

    @Override
    protected void reset() {
        ConfigCompanionWorkBenchRecipe companionEgg = new ConfigCompanionWorkBenchRecipe(createCompanionEgg());
        companionEgg.addInput(new ItemStack(ModItems.COMPANION_ESSENCE.get(), 4));
        companionWorkBenchRecipes.add(companionEgg);

//        ConfigCompanionWorkBenchRecipe companionRelic = new ConfigCompanionWorkBenchRecipe(createCompanionRelic());
//        companionRelic.addInput(new ItemStack(ModItems.COMPANION_SCRAP.get(),12));
//        companionWorkBenchRecipes.add(companionRelic);
    }

    private ItemStack createCompanionRelic() {
        return ItemStack.EMPTY;
    }

    private ItemStack createCompanionEgg() {
        ItemStack companionEgg = new ItemStack(iskallia.vault.init.ModItems.COMPANION_EGG);
        CompanionEggItem.setSeries(companionEgg, CompanionSeries.getRandomSeries(JavaRandom.ofNanoTime()));
        return companionEgg;
    }
}
