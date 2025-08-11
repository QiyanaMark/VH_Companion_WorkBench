package block.entity.custom;

import Item.ModItems;
import block.entity.custom.menus.CompanionJuicerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
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
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Random;

public class CompanionJuicerEntity extends BlockEntity implements MenuProvider {
    // Slot definitions
    public static final int INPUT_SLOT_1 = 0;  // For companion_egg
    public static final int INPUT_SLOT_2 = 1;  // For companion_particle
    public static final int INPUT_SLOT_3 = 2;  // For companion_relic
    public static final int OUTPUT_SLOT_1 = 3; // companion_essence
    public static final int OUTPUT_SLOT_2 = 4; // particle_fragment
    public static final int OUTPUT_SLOT_3 = 5; // relic_fragment

    public final ItemStackHandler itemHandler = new ItemStackHandler(6) { // 3 input, 3 output slots
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public CompanionJuicerEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.COMPANION_JUICER.get(), pPos, pBlockState);
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent("Companion Juicer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CompanionJuicerMenu(pContainerId, pPlayerInventory, this);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, CompanionJuicerEntity pBlockEntity) {
        if (hasRecipe(pBlockEntity)) {
            craftItem(pBlockEntity);
        }
    }

    private static void craftItem(CompanionJuicerEntity entity) {
        // Get Vault Hunters items dynamically (in case they change)
        Item companionEgg = ForgeRegistries.ITEMS.getValue("vault_hunters:companion_egg");
        Item companionParticle = ForgeRegistries.ITEMS.getValue("vault_hunters:companion_particle");
        Item companionRelic = ForgeRegistries.ITEMS.getValue("vault_hunters:companion_relic");

        // Check if we have all inputs
        if (entity.itemHandler.getStackInSlot(INPUT_SLOT_1).getItem() == companionEgg &&
                entity.itemHandler.getStackInSlot(INPUT_SLOT_2).getItem() == companionParticle &&
                entity.itemHandler.getStackInSlot(INPUT_SLOT_3).getItem() == companionRelic) {

            // Consume inputs
            entity.itemHandler.extractItem(INPUT_SLOT_1, 1, false);
            entity.itemHandler.extractItem(INPUT_SLOT_2, 1, false);
            entity.itemHandler.extractItem(INPUT_SLOT_3, 1, false);

            // Add outputs if there's space
            if (entity.itemHandler.getStackInSlot(OUTPUT_SLOT_1).getCount() < entity.itemHandler.getStackInSlot(OUTPUT_SLOT_1).getMaxStackSize()) {
                entity.itemHandler.insertItem(OUTPUT_SLOT_1, new ItemStack(ModItems.COMPANION_ESSENCE.get(), 1), false);
            }
            if (entity.itemHandler.getStackInSlot(OUTPUT_SLOT_2).getCount() < entity.itemHandler.getStackInSlot(OUTPUT_SLOT_2).getMaxStackSize()) {
                entity.itemHandler.insertItem(OUTPUT_SLOT_2, new ItemStack(ModItems.PARTICLE_FRAGMENT.get(), 1), false);
            }
            if (entity.itemHandler.getStackInSlot(OUTPUT_SLOT_3).getCount() < entity.itemHandler.getStackInSlot(OUTPUT_SLOT_3).getMaxStackSize()) {
                entity.itemHandler.insertItem(OUTPUT_SLOT_3, new ItemStack(ModItems.RELIC_FRAGMENT.get(), 1), false);
            }
        }
    }

    private static boolean hasRecipe(CompanionJuicerEntity entity) {
        // Get Vault Hunters items dynamically
        Item companionEgg = ForgeRegistries.ITEMS.getValue("vault_hunters:companion_egg");
        Item companionParticle = ForgeRegistries.ITEMS.getValue("vault_hunters:companion_particle");
        Item companionRelic = ForgeRegistries.ITEMS.getValue("vault_hunters:companion_relic");

        // Check if we have all inputs
        boolean hasEgg = companionEgg != null && entity.itemHandler.getStackInSlot(INPUT_SLOT_1).getItem() == companionEgg;
        boolean hasParticle = companionParticle != null && entity.itemHandler.getStackInSlot(INPUT_SLOT_2).getItem() == companionParticle;
        boolean hasRelic = companionRelic != null && entity.itemHandler.getStackInSlot(INPUT_SLOT_3).getItem() == companionRelic;

        // Check if we have space for outputs
        boolean canOutputEssence = entity.itemHandler.getStackInSlot(OUTPUT_SLOT_1).getCount() < entity.itemHandler.getStackInSlot(OUTPUT_SLOT_1).getMaxStackSize();
        boolean canOutputParticle = entity.itemHandler.getStackInSlot(OUTPUT_SLOT_2).getCount() < entity.itemHandler.getStackInSlot(OUTPUT_SLOT_2).getMaxStackSize();
        boolean canOutputRelic = entity.itemHandler.getStackInSlot(OUTPUT_SLOT_3).getCount() < entity.itemHandler.getStackInSlot(OUTPUT_SLOT_3).getMaxStackSize();

        return hasEgg && hasParticle && hasRelic && (canOutputEssence || canOutputParticle || canOutputRelic);
    }

    // Add this method to check if the player can still interact
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double) this.worldPosition.getX() + 0.5D,
                    (double) this.worldPosition.getY() + 0.5D,
                    (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    // Add this to sync data to client
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        if (tag != null) {
            load(tag);
        }
    }
}