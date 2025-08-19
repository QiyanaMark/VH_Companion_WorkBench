package net.lawliet.companionWorkStation.blocks.entity;

import iskallia.vault.block.entity.base.ForgeRecipeTileEntity;
import iskallia.vault.config.recipe.ForgeRecipeType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.lawliet.companionWorkStation.menu.CompanionWorkBenchMenu;

public class CompanionWorkBenchBlockEntity extends ForgeRecipeTileEntity implements MenuProvider {

    public CompanionWorkBenchBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COMPANION_WORKBENCH_ENTITY.get(), pos, state, 6, ForgeRecipeType.valueOf("COMPANION_WORKBENCH"));
    }

    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return createMenu(i,inventory);
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new CompanionWorkBenchMenu(i, this.getLevel(), this.getBlockPos(), inventory);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container.companion_workbench");
    }
}