package vault_work_station.recipe.companionWorkBench;

import iskallia.vault.config.CompanionRelicsConfig;
import iskallia.vault.config.recipe.ForgeRecipeType;
import iskallia.vault.container.oversized.OverSizedItemStack;
import iskallia.vault.core.random.JavaRandom;
import iskallia.vault.core.random.RandomSource;
import iskallia.vault.gear.crafting.recipe.VaultForgeRecipe;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.CompanionRelicItem;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

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
        if(player instanceof ServerPlayer serverPlayer) {
            int vaultLevel = PlayerVaultStatsData.get(serverPlayer.getServer()).getVaultStats(player.getUUID()).getVaultLevel();
            return vaultLevel >= 50;
        }
        return false;
    }

    @Override
    public ItemStack createOutput(List<OverSizedItemStack> consumed, ServerPlayer crafter, int vaultLevel) {
        ItemStack stack = this.getRawOutput();
        if(stack.is(ModItems.COMPANION_RELIC)) {
            return createRandomRelic(vaultLevel);
        }
        
        return stack;
    }

    private ItemStack createRandomRelic(int vaultLevel) {
        RandomSource random = JavaRandom.ofNanoTime();
        Optional<CompanionRelicsConfig.ResolvedEntry> entry = ModConfigs.COMPANION_RELICS.getRandom(vaultLevel, random);
        if(entry.isPresent()) {
            return CompanionRelicItem.create(entry.get().getModifiers(), entry.get().getModel().get(random));
        }
        ItemStack stack = this.getRawOutput();
        stack.setCount(vault_work_station.config.ModConfigs.COMPANION_RECYCLE.getCompanionScrapFromRecycleRelic());
        return stack;
    }
}