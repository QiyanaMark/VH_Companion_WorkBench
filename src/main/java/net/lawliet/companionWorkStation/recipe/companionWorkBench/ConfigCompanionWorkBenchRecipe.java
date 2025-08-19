package net.lawliet.companionWorkStation.recipe.companionWorkBench;

import iskallia.vault.config.entry.ItemEntry;
import iskallia.vault.config.entry.recipe.ConfigForgeRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ConfigCompanionWorkBenchRecipe extends ConfigForgeRecipe<CompanionWorkBenchRecipe> {
    public ConfigCompanionWorkBenchRecipe(ItemStack output) {
        super(output);
    }

    @Override
    public CompanionWorkBenchRecipe makeRecipe() {
        ItemStack out  = this.output.createItemStack();
        List<ItemStack> in = this.inputs.stream().map(ItemEntry::createItemStack).toList();
        return new CompanionWorkBenchRecipe(this.id, out, in);
    }
}
