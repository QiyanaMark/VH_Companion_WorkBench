package vault_work_station.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.Config;
import iskallia.vault.item.CompanionItem;
import iskallia.vault.item.CompanionParticleTrailItem;
import iskallia.vault.item.CompanionRelicItem;
import iskallia.vault.world.data.PlayerCompanionData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.server.ServerLifecycleHooks;
import vault_work_station.Items.ModItems;
import vault_work_station.VaultWorkStation;

import java.io.File;
import java.util.UUID;

public class CompanionRecycleConfig extends Config {
    @Expose
    private int smeltTimer;
    @Expose
    private int companionEssenceDrop;
    @Expose
    private int companionScrapFromRecycleRelic;
    @Expose
    private int companionScrapFromRecycleParticleTrail;
    @Expose
    private boolean giveRetiredCompanion;

    public CompanionRecycleConfig() {
        super();
        this.root = "config%s%s%s".formatted(File.separator, VaultWorkStation.MOD_ID, File.separator);
    }

    @Override
    public String getName() {
        return "companion_recycler";
    }

    @Override
    protected void reset() {
        this.smeltTimer = 200;
        this.companionEssenceDrop = 1;
        this.companionScrapFromRecycleRelic = 4;
        this.companionScrapFromRecycleParticleTrail = 1;
        this.giveRetiredCompanion = true;
    }

    public int getSmeltingTickTime() {
        return this.smeltTimer;
    }

    public int getCompanionEssenceDropCount() {
        return this.companionEssenceDrop;
    }

    public int getCompanionScrapFromRecycleRelic() {
        return this.companionScrapFromRecycleRelic;
    }

    public int getCompanionScrapFromRecycleParticleTrail() {
        return this.companionScrapFromRecycleParticleTrail;
    }

    public boolean giveRetiredCompanion() {
        return this.giveRetiredCompanion;
    }

    public static class RecycleOutput {
        private final ItemStack retiredCompanionOutput;
        private final ItemStack companionEssenceOutput;
        private final ItemStack companionScrapOutput;

        public RecycleOutput(ItemStack itemInput, CompanionRecycleConfig config) {
            retiredCompanionOutput = config.giveRetiredCompanion() ? generateRetiredCompanionOutput(itemInput) : ItemStack.EMPTY;
            companionEssenceOutput = generateCompanionEssenceOutput(itemInput, config.getCompanionEssenceDropCount());
            companionScrapOutput = generateCompanionScrapOutput(itemInput, config.getCompanionScrapFromRecycleRelic(), config.getCompanionScrapFromRecycleParticleTrail());
        }

        public ItemStack getRetiredCompanionOutput() {
            return retiredCompanionOutput;
        }

        public ItemStack getCompanionEssenceOutput() {
            return companionEssenceOutput;
        }

        public ItemStack getCompanionScrapOutput() {
            return companionScrapOutput;
        }

        public ItemStack generateRetiredCompanionOutput(ItemStack itemStack) {
            if (!(itemStack.getItem() instanceof CompanionItem)) {
                return ItemStack.EMPTY;
            }
            ItemStack companionCopy = itemStack.copy();
            CompanionItem.setCompanionHearts(companionCopy, 0);
            return companionCopy;
        }

        public ItemStack generateCompanionScrapOutput(ItemStack itemStack, int relicMultiplier, int particleTrailMultiplier) {
            Item returningItem = ModItems.COMPANION_SCRAP.get();
            if (itemStack.getItem() instanceof CompanionItem) {
                UUID companionId = CompanionItem.getCompanionUUID(itemStack);
                if (companionId != null) {
                    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                    if (server != null) {
                        PlayerCompanionData.CompanionData data = PlayerCompanionData.get(server).get(companionId).orElse(null);
                        if (data != null) {
                            int relics = data.getAllRelicMap().size();
                            int particleTrails = data.getAllCosmeticTrailTypes().size();

                            return new ItemStack(returningItem, relics * relicMultiplier + particleTrails * particleTrailMultiplier);
                        }
                    }


                }

            }
            if (itemStack.getItem() instanceof CompanionRelicItem) {
                return new ItemStack(returningItem, itemStack.getCount() * relicMultiplier);
            }
            if (itemStack.getItem() instanceof CompanionParticleTrailItem) {
                return new ItemStack(returningItem, itemStack.getCount() * particleTrailMultiplier);
            }
            return ItemStack.EMPTY;
        }

        public ItemStack generateCompanionEssenceOutput(ItemStack itemStack, int companionEssenceCount) {
            if (!(itemStack.getItem() instanceof CompanionItem)) {
                return ItemStack.EMPTY;
            }
            return new ItemStack(ModItems.COMPANION_ESSENCE.get(), companionEssenceCount);
        }

    }

}
