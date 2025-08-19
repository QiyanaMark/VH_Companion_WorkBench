package net.lawliet.companionWorkStation.screen;

import iskallia.vault.client.gui.framework.element.CraftingSelectorElement;
import iskallia.vault.client.gui.screen.block.base.ForgeRecipeContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.lawliet.companionWorkStation.blocks.entity.CompanionWorkBenchBlockEntity;
import net.lawliet.companionWorkStation.menu.CompanionWorkBenchMenu;

public class CompanionWorkBenchScreen extends ForgeRecipeContainerScreen<CompanionWorkBenchBlockEntity,CompanionWorkBenchMenu> {
    public CompanionWorkBenchScreen(CompanionWorkBenchMenu container, Inventory inventory, Component title) {
        super(container, inventory, title, 173);
    }

    @Override
    protected CraftingSelectorElement<?> createCraftingSelector() {
        return this.makeCraftingSelector();
    }
}