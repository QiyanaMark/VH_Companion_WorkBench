package vault_work_station.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.openjdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import vault_work_station.blocks.entity.CompanionRecyclerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import vault_work_station.blocks.ModBlocks;

public class CompanionRecyclerMenu extends AbstractContainerMenu {
    private final BlockPos pos;
    private final IItemHandler blockHandler;
    private final Inventory playerInv; // Store player inventory reference

    // Server-side constructor
    public CompanionRecyclerMenu(int id, Inventory playerInv, BlockPos pos) {
        super(ModMenuTypes.COMPANION_RECYCLER_MENU.get(), id);
        this.pos = pos;
        this.playerInv = playerInv; // Store the player inventory

        BlockEntity be = playerInv.player.level.getBlockEntity(pos);
        if (be instanceof CompanionRecyclerBlockEntity sb) {
            this.blockHandler = sb.getItemHandler();
        } else {
            throw new ValueException("Not a Companion recycler at %s".formatted(be.getBlockPos()));
        }

        // Block slots - VERTICAL LAYOUT
        this.addSlot(new SlotItemHandler(blockHandler, 0, 79, 17)); // Input slot (top center)
        this.addSlot(new ResultSlot(blockHandler, 1, 55, 54)); // Output slot for retired companion (bottom left)
        this.addSlot(new ResultSlot(blockHandler, 2, 79, 54)); // Output slot for companion essence (bottom center)
        this.addSlot(new ResultSlot(blockHandler, 3, 103, 54)); // Output slot for companion scrap (bottom right)

        // Player inventory
        int startX = 8;
        int startY = 84;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, startX + col * 18 - 1, startY + row * 18 - 1));
            }
        }

        // Player hotbar
        for (int hotbar = 0; hotbar < 9; ++hotbar) {
            this.addSlot(new Slot(playerInv, hotbar, startX + hotbar * 18 - 1, startY + 57));
        }
    }

    // Client-side constructor
    public CompanionRecyclerMenu(int id, Inventory playerInv, FriendlyByteBuf data) {
        this(id, playerInv, data != null ? data.readBlockPos() : BlockPos.ZERO);
    }

    // Update stillValid to handle null/zero positions
    @Override
    public boolean stillValid(@NotNull Player player) {
        if (pos.equals(BlockPos.ZERO)) {
            return false; // Invalid position
        }
        return stillValid(ContainerLevelAccess.create(player.level, pos), player, ModBlocks.COMPANION_RECYCLER_BLOCK.get());
    }

    @NotNull
    @Override
    public ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            final int containerSize = blockHandler.getSlots();
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
        if (!playerInv.player.level.isClientSide) {
            BlockEntity be = playerInv.player.level.getBlockEntity(pos);
            if (be instanceof CompanionRecyclerBlockEntity smelter) {
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
        public boolean mayPlace(@NotNull ItemStack stack) {
            return false;
        }
    }
}