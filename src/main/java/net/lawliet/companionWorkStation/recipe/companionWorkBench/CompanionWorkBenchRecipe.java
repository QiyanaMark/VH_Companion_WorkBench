package net.lawliet.companionWorkStation.recipe.companionWorkBench;

import iskallia.vault.config.CompanionRelicsConfig;
import iskallia.vault.config.recipe.ForgeRecipeType;
import iskallia.vault.container.oversized.OverSizedItemStack;
import iskallia.vault.core.random.JavaRandom;
import iskallia.vault.core.random.RandomSource;
import iskallia.vault.gear.crafting.recipe.VaultForgeRecipe;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.CompanionEggItem;
import iskallia.vault.item.CompanionParticleTrailItem;
import iskallia.vault.item.CompanionRelicItem;
import iskallia.vault.item.CompanionSeries;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.Optional;

public class CompanionWorkBenchRecipe extends VaultForgeRecipe {

    public CompanionWorkBenchRecipe(ResourceLocation id, ItemStack output, List<ItemStack> inputs) {
        super(ForgeRecipeType.valueOf("COMPANION_WORKBENCH"), id, output, inputs);
    }

    public CompanionWorkBenchRecipe(Object o, Object o1) {
        super(ForgeRecipeType.valueOf("COMPANION_WORKBENCH"), (ResourceLocation) o, (ItemStack) o1);
    }

    @Override
    public boolean canCraft(Player player) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        int vaultLevel = PlayerVaultStatsData.get(server).getVaultStats(player.getUUID()).getVaultLevel();
        return vaultLevel >= 50;
    }

    @Override
    public ItemStack createOutput(List<OverSizedItemStack> consumed, ServerPlayer crafter, int vaultLevel) {
        ItemStack stack = this.getRawOutput();
        if(stack.is(ModItems.COMPANION_EGG)) {
            return createRandomEgg();
        }
        if(stack.is(ModItems.COMPANION_RELIC)) {
            return createRandomRelic(vaultLevel);
        }
        if(stack.is(ModItems.COMPANION_PARTICLE_TRAIL)) {
            return createRandomParticleTrails();
        }
        
        return stack;
    }

    private ItemStack createRandomParticleTrails() {
        return CompanionParticleTrailItem.createRandom();
    }

    private ItemStack createRandomEgg() {
        ItemStack companionEgg = new ItemStack(ModItems.COMPANION_EGG);
        CompanionEggItem.setSeries(companionEgg, CompanionSeries.getRandomSeries(JavaRandom.ofNanoTime()));
        return companionEgg;
    }

    private ItemStack createRandomRelic(int vaultLevel) {
        RandomSource random = JavaRandom.ofNanoTime();
        Optional<CompanionRelicsConfig.ResolvedEntry> entry = ModConfigs.COMPANION_RELICS.getRandom(vaultLevel, random);
        if(entry.isPresent()) {
            return CompanionRelicItem.create(entry.get().getModifiers(), entry.get().getModel().get(random));
        }
        ItemStack stack = this.getRawOutput();
        stack.setCount(net.lawliet.companionWorkStation.config.ModConfigs.COMPANION_RECYCLE.getCompanionScrapFromRecycleRelic());
        return stack;
    }

    @Override
    public ItemStack getDisplayOutput(int vaultLevel) {
        if(this.getRawOutput().getItem() instanceof  CompanionEggItem) {
            ItemStack companionEgg = new ItemStack(ModItems.COMPANION_EGG);
            CompanionEggItem.setSeries(companionEgg, CompanionSeries.PET);
            return companionEgg;
        }
        return super.getRawOutput();
    }


}