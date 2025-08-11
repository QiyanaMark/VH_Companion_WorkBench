package vault_work_station.blocks.custom;

import vault_work_station.blocks.entity.ModBlockEntities;
import vault_work_station.screen.ModMenusTypes;
import vault_work_station.Item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class SmelterBlockEntity extends BlockEntity implements net.minecraft.world.MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    private int cookTime = 0;
    private static final int COOK_TIME_TOTAL = 200;

    public SmelterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SMELTER_ENTITY.get(), pos, state);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        net.minecraft.world.level.block.entity.BlockEntity.dropContents(level, worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SmelterBlockEntity entity) {
        if(level.isClientSide) return;

        if(entity.canSmelt()) {
            entity.cookTime++;
            if(entity.cookTime >= COOK_TIME_TOTAL) {
                entity.cookTime = 0;
                entity.smeltItem();
            }
        } else {
            entity.cookTime = 0;
        }
    }

    private boolean canSmelt() {
        ItemStack input = itemHandler.getStackInSlot(0);
        if(input.isEmpty()) return false;

        ItemStack output = itemHandler.getStackInSlot(1);

        ItemStack result = getSmeltingResult(input);
        if(result.isEmpty()) return false;

        if(output.isEmpty()) return true;

        if(!output.sameItem(result)) return false;

        int outputCount = output.getCount() + result.getCount();
        return outputCount <= getMaxStackSize() && outputCount <= output.getMaxStackSize();
    }

    private void smeltItem() {
        if (!canSmelt()) return;

        ItemStack input = itemHandler.getStackInSlot(0);
        ItemStack result = getSmeltingResult(input);
        ItemStack output = itemHandler.getStackInSlot(1);

        if(output.isEmpty()) {
            itemHandler.setStackInSlot(1, result.copy());
        } else if(output.sameItem(result)) {
            output.grow(result.getCount());
        }

        input.shrink(1);
        itemHandler.setStackInSlot(0, input);
    }

    private ItemStack getSmeltingResult(ItemStack input) {
        Item item = input.getItem();

        // Here replace with your Vault Hunters mod items for smelting input
        // For demo, check by registry name strings (replace with real items or add mod deps)
        String regName = item.getRegistryName() != null ? item.getRegistryName().toString() : "";

        if(regName.equals("vaulthunters:companion_egg")) {
            return new ItemStack(ModItems.COMPANION_ESSENCE.get());
        } else if(regName.equals("vaulthunters:relic")) {
            return new ItemStack(ModItems.RELIC_FRAGMENT.get());
        } else if(regName.equals("vaulthunters:particle_effect")) {
            return new ItemStack(ModItems.PARTICLE_FRAGMENT.get());
        }

        return ItemStack.EMPTY;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        cookTime = tag.getInt("CookTime");
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("CookTime", cookTime);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Smelter");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new ModMenusTypes.SmelterMenu(id, playerInventory, this);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public int getCookTime() {
        return cookTime;
    }

    public int getCookTimeTotal() {
        return COOK_TIME_TOTAL;
    }
}
