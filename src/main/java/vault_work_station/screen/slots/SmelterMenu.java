package vault_work_station.screen.slots;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import vault_work_station.blocks.entity.custom.SmelterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import vault_work_station.blocks.ModBlocks;

public class SmelterMenu extends AbstractContainerMenu {
    private final BlockPos pos;
    private final IItemHandler blockHandler;
    private final Inventory playerInv; // Store player inventory reference

    // Server-side constructor
    public SmelterMenu(int id, Inventory playerInv, BlockPos pos) {
        super(ModMenuTypes.SMELTER_MENU.get(), id);
        this.pos = pos;
        this.playerInv = playerInv; // Store the player inventory

        BlockEntity be = playerInv.player.level.getBlockEntity(pos);
        if (be instanceof SmelterBlockEntity sb) {
            this.blockHandler = sb.getItemHandler();
        } else {
            this.blockHandler = new ItemStackHandler(2);
        }

        // Block slots - VERTICAL LAYOUT
        this.addSlot(new SlotItemHandler(blockHandler, 0, 80, 11)); // Input slot (top center)
        this.addSlot(new ResultSlot(blockHandler, 1, 80, 59)); // Output slot (bottom center)

        // Player inventory
        int startX = 8;
        int startY = 84;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, startX + col * 18, startY + row * 18));
            }
        }

        // Player hotbar
        for (int hotbar = 0; hotbar < 9; ++hotbar) {
            this.addSlot(new Slot(playerInv, hotbar, startX + hotbar * 18, startY + 58));
        }
    }

    // Client-side constructor
    public SmelterMenu(int id, Inventory playerInv, FriendlyByteBuf data) {
        this(id, playerInv, data != null ? data.readBlockPos() : BlockPos.ZERO);
    }

    // Update stillValid to handle null/zero positions
    @Override
    public boolean stillValid(Player player) {
        if (pos.equals(BlockPos.ZERO)) {
            return false; // Invalid position
        }
        return stillValid(ContainerLevelAccess.create(player.level, pos), player, ModBlocks.SMELTER_BLOCK.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            final int containerSize = 2;
            final int inventorySize = 36;

            if (index < containerSize) {
                if (!this.moveItemStackTo(itemstack1, containerSize, inventorySize + containerSize, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    public int getSmeltProgress() {
        if (playerInv.player != null && !playerInv.player.level.isClientSide) {
            BlockEntity be = playerInv.player.level.getBlockEntity(pos);
            if (be instanceof SmelterBlockEntity smelter) {
                return smelter.getSmeltProgressScaled(24);
            }
        }
        return 0;
    }

    private static class ResultSlot extends SlotItemHandler {
        public ResultSlot(IItemHandler handler, int index, int x, int y) {
            super(handler, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}