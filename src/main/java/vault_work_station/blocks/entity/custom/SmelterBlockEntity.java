package vault_work_station.blocks.entity.custom;
import net.minecraft.network.chat.Component;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import vault_work_station.blocks.entity.ModBlockEntities;
import vault_work_station.Item.ModItems;
import vault_work_station.screen.slots.SmelterMenu;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SmelterBlockEntity extends BlockEntity implements MenuProvider {
    // 0 = input, 1 = output
    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    private int smeltTime = 0;
    private static final int SMELT_TIME_TOTAL = 200; // ticks (10s)

    public SmelterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SMELTER_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SmelterBlockEntity be) {
        if (level.isClientSide) return;

        // Get the closest player within 5 blocks
        Player player = level.getNearestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5, null);

        ItemStack input = be.itemHandler.getStackInSlot(0);
        ItemStack output = be.itemHandler.getStackInSlot(1);

        if (be.canSmelt(input, output)) {
            be.smeltTime++;
            if (be.smeltTime >= SMELT_TIME_TOTAL) {
                be.doSmelt(player); // Pass the player here
                be.smeltTime = 0;
            }
            be.setChanged();
        } else {
            if (be.smeltTime > 0) be.smeltTime = 0;
        }
    }

    private boolean canSmelt(ItemStack in, ItemStack out) {
        if (in.isEmpty()) return false;
        ItemStack result = getResult(in);
        if (result.isEmpty()) return false;
        if (out.isEmpty()) return true;
        if (!out.sameItem(result)) return false;
        return out.getCount() + result.getCount() <= out.getMaxStackSize();
    }

    private void doSmelt(@Nullable Player player) {  // Add @Nullable annotation
        ItemStack in = itemHandler.getStackInSlot(0);
        ItemStack result = getResult(in);
        ItemStack out = itemHandler.getStackInSlot(1);

        // Delete companion UUID from player data if present
        if (player != null && in.hasTag()) {
            CompoundTag tag = in.getTag();
            if (tag != null && tag.hasUUID("uuid")) {
                player.getPersistentData().remove("vault_companions." + tag.getUUID("uuid"));
            }
        }

        // Original smelting logic
        if (out.isEmpty()) {
            itemHandler.setStackInSlot(1, result.copy());
        } else if (out.sameItem(result)) {
            out.grow(result.getCount());
        }
        in.shrink(1);
    }

    // Hardcoded allowed inputs -> outputs
    private ItemStack getResult(ItemStack stack) {
        // Vault Hunters inputs → Your mod's outputs
        if (stack.getItem().getRegistryName().toString().equals("the_vault:companion")) {
            return new ItemStack(ModItems.COMPANION_ESSENCE.get());
        }
        else if (stack.getItem().getRegistryName().toString().equals("the_vault:companion_particle_trail")) {
            return new ItemStack(ModItems.PARTICLE_FRAGMENT.get());
        }
        else if (stack.getItem().getRegistryName().toString().equals("the_vault:companion_relic")) {
            return new ItemStack(ModItems.RELIC_FRAGMENT.get());
        }
        return ItemStack.EMPTY;
    }

    // NBT save/load
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inv", itemHandler.serializeNBT());
        tag.putInt("smeltTime", smeltTime);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inv"));
        smeltTime = tag.getInt("smeltTime");
    }

    // Capability (for hoppers, menus)
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable net.minecraft.core.Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    // MenuProvider methods
    public net.minecraft.network.chat.Component getDisplayName() {
        return new net.minecraft.network.chat.TranslatableComponent("container.smelter_block");
    }


    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, net.minecraft.world.entity.player.Inventory inv, Player player) {
        return new SmelterMenu(id, inv, this.getBlockPos());
    }

    // Expose item handler to menu
    public IItemHandler getItemHandler() {
        return itemHandler;
    }
}
