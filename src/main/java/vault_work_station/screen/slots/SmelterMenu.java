package vault_work_station.screen.slots;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import vault_work_station.blocks.entity.custom.SmelterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import vault_work_station.blocks.ModBlocks;

public class SmelterMenu extends AbstractContainerMenu {
    private final BlockPos pos;
    private IItemHandler blockHandler;

    // server-side constructor
    public SmelterMenu(int id, Inventory playerInv, BlockPos pos) {
        super(ModMenuTypes.SMELTER_MENU.get(), id);
        this.pos = pos;
        // try get block entity's item handler
        BlockEntity be = playerInv.player.level.getBlockEntity(pos);
        if (be instanceof SmelterBlockEntity sb) {
            this.blockHandler = sb.getItemHandler();
        } else {
            // fallback empty handler, avoid NPEs (client will override)
            this.blockHandler = new net.minecraftforge.items.ItemStackHandler(2);
        }

        // Block slots: input (0) at 56,35 ; output (1) at 116,35
        this.addSlot(new SlotItemHandler(blockHandler, 0, 56, 35)); // input
        this.addSlot(new ResultSlot(blockHandler, 1, 116, 35)); // output

        // Player inventory
        int startX = 8;
        int startY = 84;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, startX + col * 18, startY + row * 18));
            }
        }
        for (int hot = 0; hot < 9; ++hot) {
            this.addSlot(new Slot(playerInv, hot, startX + hot * 18, startY + 58));
        }
    }

    // client constructor used by IForgeMenuType.create -> SmelterMenu(windowId, inv, buf.readBlockPos())
    public SmelterMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv, buf.readBlockPos());
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(net.minecraft.world.inventory.ContainerLevelAccess.create(player.level, pos), player, ModBlocks.SMELTER_BLOCK.get());
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.getItem() != ItemStack.EMPTY) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();

            final int containerSlots = 2;
            final int playerStart = containerSlots;
            final int playerEnd = this.slots.size();

            if (index < containerSlots) {
                // from block to player
                if (!this.moveItemStackTo(slotStack, playerStart, playerEnd, true)) return ItemStack.EMPTY;
            } else {
                // from player to block (try input slot)
                // check if smeltable
                // We can't call BE directly here in a safe way; just try move to input
                if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();

            if (slotStack.getCount() == stack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, slotStack);
        }
        return stack;
    }

    // Prevent inserting into output slot
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
