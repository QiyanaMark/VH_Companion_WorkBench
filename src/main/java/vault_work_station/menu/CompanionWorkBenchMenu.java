package vault_work_station.menu;

import iskallia.vault.container.spi.ForgeRecipeContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import vault_work_station.blocks.entity.CompanionWorkBenchBlockEntity;

import java.awt.*;
import java.util.function.Predicate;

public class CompanionWorkBenchMenu extends ForgeRecipeContainer<CompanionWorkBenchBlockEntity> {

    public CompanionWorkBenchMenu(int id, Level world, BlockPos pos, Inventory playerInventory) {
        super(ModMenuTypes.COMPANION_WORKBENCH_MENU.get(), id, world, pos, playerInventory);
    }

    @Override
    protected Class<CompanionWorkBenchBlockEntity> getTileClass() {
        return CompanionWorkBenchBlockEntity.class;
    }

    @Override
    public Point getOffset() {
        return new Point(8, 23);
    }

    @Override
    protected @Nullable ResourceLocation getBackgroundTextureForSlot(int i) {
        return null;
    }

    @Override
    protected @Nullable Predicate<ItemStack> getFilterForSlot(int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.getTile() != null && this.getTile().stillValid(player);
    }
}