package block.entity.custom.menus;

import block.entity.custom.CompanionJuicerEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;



import static block.entity.custom.menus.ModMenuTypes.COMPANION_JUICER_MENU;;

public class CompanionJuicerMenu extends AbstractContainerMenu {
    public final CompanionJuicerEntity blockEntity;
    private final Player player;
    private final IItemHandler playerInventory;

    public CompanionJuicerMenu(int containerId, Inventory playerInventory, CompanionJuicerEntity entity) {
        super(COMPANION_JUICER_MENU.get(), containerId);
        this.blockEntity = entity;
        this.player = playerInventory.player;
        this.playerInventory = new InvWrapper(playerInventory);

        if (blockEntity != null) {
            addSlot(new SlotItemHandler(blockEntity.itemHandler, CompanionJuicerEntity.INPUT_SLOT_1, 56, 17));
            addSlot(new SlotItemHandler(blockEntity.itemHandler, CompanionJuicerEntity.INPUT_SLOT_2, 56, 35));
            addSlot(new SlotItemHandler(blockEntity.itemHandler, CompanionJuicerEntity.INPUT_SLOT_3, 56, 53));

            addSlot(new OutputSlot(blockEntity.itemHandler, CompanionJuicerEntity.OUTPUT_SLOT_1, 116, 17));
            addSlot(new OutputSlot(blockEntity.itemHandler, CompanionJuicerEntity.OUTPUT_SLOT_2, 116, 35));
            addSlot(new OutputSlot(blockEntity.itemHandler, CompanionJuicerEntity.OUTPUT_SLOT_3, 116, 53));
        }
        layoutPlayerInventorySlots(8, 84);
    }

    public CompanionJuicerMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, getBlockEntity(playerInventory, extraData));
    }

    private static CompanionJuicerEntity getBlockEntity(Inventory playerInventory, FriendlyByteBuf extraData) {
        BlockEntity entity = playerInventory.player.level.getBlockEntity(extraData.readBlockPos());
        if (entity instanceof CompanionJuicerEntity) {
            return (CompanionJuicerEntity) entity;
        }
        throw new IllegalStateException("Wrong block entity at: " + extraData.readBlockPos());
    }

    private static class OutputSlot extends SlotItemHandler {
        public OutputSlot(IItemHandler itemHandler, int index, int x, int y) {
            super(itemHandler, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < 6) {
                if (!this.moveItemStackTo(itemstack1, 6, 42, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 3, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity != null &&
                blockEntity.stillValid(player);
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);
        addSlotRange(playerInventory, 0, leftCol, topRow + 58, 9, 18);
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private void addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
    }
}