package vault_work_station.screen;

import iskallia.vault.client.gui.framework.element.CraftingSelectorElement;
import iskallia.vault.client.gui.screen.block.base.ForgeRecipeContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import vault_work_station.blocks.entity.CompanionWorkBenchBlockEntity;
import vault_work_station.menu.CompanionWorkBenchMenu;
import vault_work_station.VaultWorkStation;

public class CompanionWorkBenchScreen extends ForgeRecipeContainerScreen<CompanionWorkBenchBlockEntity,CompanionWorkBenchMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(VaultWorkStation.MOD_ID, "textures/gui/companion_workbench_gui.png");

    public CompanionWorkBenchScreen(CompanionWorkBenchMenu container, Inventory inventory, Component title) {
        super(container, inventory, title, 173);
    }

    @Override
    protected @NotNull CraftingSelectorElement<?> createCraftingSelector() {
        return this.makeCraftingSelector();
    }
}