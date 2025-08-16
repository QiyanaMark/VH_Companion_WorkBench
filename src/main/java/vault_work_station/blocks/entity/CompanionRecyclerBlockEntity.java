package vault_work_station.blocks.entity;

import iskallia.vault.init.ModNetwork;
import iskallia.vault.item.CompanionItem;
import iskallia.vault.item.CompanionParticleTrailItem;
import iskallia.vault.item.CompanionRelicItem;
import iskallia.vault.network.message.RecyclerParticleMessage;
import iskallia.vault.world.data.PlayerCompanionData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;
import vault_work_station.config.CompanionRecycleConfig;
import vault_work_station.config.ModConfigs;
import vault_work_station.menu.CompanionRecyclerMenu;
import vault_work_station.utils.MiscUtilsAdditions;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

public class CompanionRecyclerBlockEntity extends BlockEntity implements MenuProvider {
    // 0 = input, 1,2,3 = output
    private final CompanionRecyclerInventoryHandler itemHandler = new CompanionRecyclerInventoryHandler(4);
    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    private int smeltTime = 0;
//    private static final int SMELT_TIME_TOTAL = 200; // ticks (10s)

    public CompanionRecyclerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COMPANION_RECYCLER_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CompanionRecyclerBlockEntity be) {
        if (level.isClientSide) return;
        // Get the closest player within 5 blocks
        if (!be.canSmelt()) {
            be.resetProcess(level);
        }
        else {
            be.smeltTime++;
            if (be.smeltTime >= ModConfigs.COMPANION_RECYCLE.getSmeltingTickTime()) {

                be.doSmelt(); // Pass the player here
                be.smeltTime = 0;
            }
            be.setChanged();
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendBlockUpdated(pos, state, state, 3);
            }
        }

    }

    private void resetProcess(@Nullable Level level) {
        this.startProcess(level);
    }

    private void startProcess(@Nullable Level level) {
        int prevTick = this.smeltTime;
        this.smeltTime = 0;
        this.setChanged();
        if (prevTick != this.smeltTime && level instanceof ServerLevel serverLevel) {
            serverLevel.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    private void triggerItemChange() {
        ItemStack input = this.itemHandler.getStackInSlot(0);
        if (!this.isValidInput(input)) {
            this.resetProcess(null);
        }
        else {
            this.startProcess(null);
        }
    }

    private boolean isValidInput(ItemStack input) {
        Item inputItem = input.getItem();
        return inputItem instanceof CompanionItem || inputItem instanceof CompanionRelicItem || inputItem instanceof CompanionParticleTrailItem;
    }

    private boolean canSmelt() {
        CompanionRecycleConfig.RecycleOutput result = getResults(this.itemHandler.getStackInSlot(0));
        if(result == null) {
            return false;
        }
        if(!MiscUtilsAdditions.canFullyMergeIntoSlot(this.itemHandler, 1, result.getRetiredCompanionOutput())) {
            return false;
        }
        if(!MiscUtilsAdditions.canFullyMergeIntoSlot(this.itemHandler, 2, result.getCompanionEssenceOutput())) {
            return false;
        }
        return  MiscUtilsAdditions.canFullyMergeIntoSlot(this.itemHandler, 3, result.getCompanionScrapOutput());
    }



    private void doSmelt() {
        ItemStack in = itemHandler.getStackInSlot(0).copy();
        CompanionRecycleConfig.RecycleOutput result = getResults(in);

        // Delete companion UUID from player data if present
        BlockPos pos = this.getBlockPos();
        if(this.level != null) {
            this.level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 0.5F + (new Random()).nextFloat() * 0.25F, 0.75F + (new Random()).nextFloat() * 0.25F);
            if (!ModConfigs.COMPANION_RECYCLE.giveRetiredCompanion() && in.hasTag()) {
                // Player fetching logic needs to be changed
                Player player = this.level.getNearestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5, null);
                CompoundTag tag = in.getTag();
                if (tag != null && tag.hasUUID("uuid") && player != null) {
                    player.getPersistentData().remove("vault_companions." + tag.getUUID("uuid"));
                }
            }
        }
        if(result != null) {
//            itemHandler.insertItem(1, result.getRetiredCompanionOutput(),false);
//            itemHandler.setStackInSlot(2, result.getCompanionEssenceOutput(), false);
//            itemHandler.setStackInSlot(3, result.getCompanionScrapOutput());
            MiscUtilsAdditions.insertIntoSlot(itemHandler, 1, result.getRetiredCompanionOutput());
            MiscUtilsAdditions.insertIntoSlot(itemHandler, 2, result.getCompanionEssenceOutput());
            MiscUtilsAdditions.insertIntoSlot(itemHandler, 3, result.getCompanionScrapOutput());
            itemHandler.getStackInSlot(0).shrink(1);
            ModNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new RecyclerParticleMessage(pos));
        }
        // Original smelting logic
    }

    // Hardcoded allowed inputs -> outputs
    @Nullable
    private CompanionRecycleConfig.RecycleOutput getResults(ItemStack stack) {
        // Vault Hunters inputs → Your mod's outputs
        if (stack.isEmpty()) {
            return null;
        }
        return new CompanionRecycleConfig.RecycleOutput(stack, ModConfigs.COMPANION_RECYCLE);
    }
    public int getSmeltProgressScaled(int scale) {
        return this.smeltTime * scale / ModConfigs.COMPANION_RECYCLE.getSmeltingTickTime();
    }

    // NBT save/load
    @Override
    public void onLoad() {
        super.onLoad();
        handler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        handler.invalidate();
    }


    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inv", itemHandler.serializeNBT());
        tag.putInt("smeltTime", smeltTime);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inv"));
        smeltTime = tag.getInt("smeltTime");
    }

    // Capability (for hoppers, menus)
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable net.minecraft.core.Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    // MenuProvider methods
    public Component getDisplayName() {
        return new TranslatableComponent("menu.companion_recycler");
    }


    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, net.minecraft.world.entity.player.Inventory inv, Player player) {
        return new CompanionRecyclerMenu(id, inv, this.getBlockPos());
    }

    // Expose item handler to menu
    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    public void drops(Level level, BlockPos pos) {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i=0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(level, pos, inventory);
    }

    public class CompanionRecyclerInventoryHandler extends ItemStackHandler {

        public CompanionRecyclerInventoryHandler(int size) {
            super(size);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if(stack.isEmpty()) {
                return false;
            }
            if (slot == 0) {
                Item inputItem = stack.getItem();
                if (inputItem instanceof CompanionItem) {
                    return !isCompanionRetired(stack);
                }
                return inputItem instanceof CompanionRelicItem || inputItem instanceof CompanionParticleTrailItem;
            }
            return false;
        }

        private static boolean isCompanionRetired(ItemStack stack) {
            if (!(stack.getItem() instanceof CompanionItem)) {
                return true;
            }
            UUID companionId = CompanionItem.getCompanionUUID(stack);
            if (companionId == null) {
                return true;
            }
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server == null) {
                return true;
            }
            PlayerCompanionData.CompanionData data = PlayerCompanionData.get(server).get(companionId).orElse(null);
            if (data == null) {
                return true;
            }
            int companionHealth = data.getHearts();
            return companionHealth <= 0;
        }

        @Override
        protected void onContentsChanged(int slot) {
            CompanionRecyclerBlockEntity.this.setChanged();
            CompanionRecyclerBlockEntity.this.triggerItemChange();
        }
    }

}
