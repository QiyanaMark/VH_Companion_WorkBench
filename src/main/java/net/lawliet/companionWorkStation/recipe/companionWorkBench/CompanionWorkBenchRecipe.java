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
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
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
        if (player instanceof ServerPlayer sPlayer) {
            int vaultLevel = PlayerVaultStatsData.get(sPlayer.server)
                    .getVaultStats(sPlayer.getUUID())
                    .getVaultLevel();
            return vaultLevel >= 50;
        }
        // Client side – use synced overlay data
        return VaultBarOverlay.vaultLevel >= 50;
    }

    @Override
    public void addCraftingDisplayTooltip(ItemStack result, List<Component> out) {
        if(result.is(ModItems.COMPANION_EGG)) {
            Tag hatchTime = result.getTag() == null ? null : result.getOrCreateTag().get("HatchTime");
            if (hatchTime instanceof NumericTag numeric) {
                int time = numeric.getAsInt();
                int minutes = time / 60;
                int seconds = time % 60;
                out.add((new TextComponent("Hatch Time: ")).withStyle((style) -> style.withColor(16777215)).append((new TextComponent(String.format("%d:%02d", minutes, seconds))).withStyle((style) -> style.withColor(11583738))));
                out.add((new TextComponent("Series: ")).withStyle((style) -> style.withColor(16777215)).append((new TextComponent("Random")).withStyle((style) -> style.withColor(11583738))));
            }
        }
        else if(result.is(ModItems.COMPANION_PARTICLE_TRAIL)) {
            out.add((new TextComponent("Random Particle Trail").withStyle(ChatFormatting.GOLD)));
        } else if(result.is(ModItems.COMPANION_RELIC)) {
            out.add((new TextComponent("Random Relic").withStyle(ChatFormatting.GOLD)));
        }

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
            CompanionEggItem.setRemainingTime(companionEgg, 20);
            return companionEgg;
        }
        return super.getRawOutput();
    }


}
