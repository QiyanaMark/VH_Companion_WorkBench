//package vault_work_station.menu;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.*;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraftforge.items.SlotItemHandler;
//import vault_work_station.blocks.entity.CompanionWorkBenchBlockEntity;
//
//public class CompanionWorkBenchMenu extends AbstractContainerMenu {
//    private final CompanionWorkBenchBlockEntity blockEntity;
//    private final Player player;
//
//    public CompanionWorkBenchMenu(int id, Inventory playerInventory, BlockPos pos) {
//        super(ModMenuTypes.COMPANION_WORKBENCH_MENU.get(), id);
//        this.player = playerInventory.player;
//
//        BlockEntity entity = player.level.getBlockEntity(pos);
//        if (!(entity instanceof CompanionWorkBenchBlockEntity)) {
//            throw new IllegalStateException("Invalid block entity at " + pos);
//        }
//        this.blockEntity = (CompanionWorkBenchBlockEntity) entity;
//
//        // Input slots (left side)
//        addSlot(new SlotItemHandler(blockEntity.getInputHandler(), 0, 44, 35));
//        addSlot(new SlotItemHandler(blockEntity.getInputHandler(), 1, 44, 53));
//
//        // Output slots (middle - non-interactable)
//        for (int i = 0; i < 3; i++) {
//            addSlot(new SlotItemHandler(blockEntity.getOutputHandler(), i, 80, 35 + i * 18) {
//                @Override public boolean mayPlace(ItemStack stack) { return false; }
//            });
//        }
//
//        // Player inventory
//        for (int row = 0; row < 3; ++row) {
//            for (int col = 0; col < 9; ++col) {
//                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
//            }
//        }
//
//        // Hotbar
//        for (int col = 0; col < 9; ++col) {
//            addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
//        }
//    }
//
//    @Override
//    public ItemStack quickMoveStack(Player player, int index) {
//        ItemStack itemstack = ItemStack.EMPTY;
//        Slot slot = slots.get(index);
//
//        if (slot != null && slot.hasItem()) {
//            ItemStack stackInSlot = slot.getItem();
//            itemstack = stackInSlot.copy();
//
//            if (index < 5) { // Workbench slots
//                if (!moveItemStackTo(stackInSlot, 5, slots.size(), true)) {
//                    return ItemStack.EMPTY;
//                }
//            } else if (!moveItemStackTo(stackInSlot, 0, 2, false)) {
//                return ItemStack.EMPTY;
//            }
//
//            if (stackInSlot.isEmpty()) {
//                slot.set(ItemStack.EMPTY);
//            } else {
//                slot.setChanged();
//            }
//        }
//        return itemstack;
//    }
//
//    @Override
//    public boolean stillValid(Player player) {
//        return blockEntity != null &&
//                blockEntity.getLevel() != null &&
//                blockEntity.getLevel().getBlockEntity(blockEntity.getBlockPos()) == blockEntity;
//    }
//}