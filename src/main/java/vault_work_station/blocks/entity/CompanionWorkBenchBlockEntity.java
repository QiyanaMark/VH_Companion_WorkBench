package vault_work_station.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vault_work_station.recipe.CompanionWorkBenchRecipe;
import vault_work_station.recipe.ModRecipeTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class CompanionWorkBenchBlockEntity extends BlockEntity implements Container {
    private final ItemStackHandler inputHandler = new ItemStackHandler(2) {
        @Override protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide) findAndCraftRecipe();
        }
    };

    private final ItemStackHandler outputHandler = new ItemStackHandler(3) {
        @Override public boolean isItemValid(int slot, ItemStack stack) { return false; }
    };

    private final LazyOptional<IItemHandler> inputOptional = LazyOptional.of(() -> inputHandler);
    private final LazyOptional<IItemHandler> outputOptional = LazyOptional.of(() -> outputHandler);

    public CompanionWorkBenchBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COMPANION_WORKBENCH_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CompanionWorkBenchBlockEntity entity) {
        if (!level.isClientSide) entity.findAndCraftRecipe();
    }

    private void findAndCraftRecipe() {
        Optional<CompanionWorkBenchRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(ModRecipeTypes.COMPANION_RECIPE_TYPE.get(), this, level);

        recipe.ifPresent(this::tryCraft);
    }

    private void tryCraft(CompanionWorkBenchRecipe recipe) {
        if (hasSpaceForOutput() && hasMaterials(recipe)) craft(recipe);
    }

    private boolean hasMaterials(CompanionWorkBenchRecipe recipe) {
        // Check input slots
        for (int i = 0; i < inputHandler.getSlots(); i++) {
            if (!recipe.getIngredients().get(i).test(inputHandler.getStackInSlot(i))) {
                return false;
            }
        }

        // Check additional costs
        return recipe.getAdditionalCosts().stream()
                .allMatch(req -> {
                    for (int i = 0; i < inputHandler.getSlots(); i++) {
                        ItemStack stack = inputHandler.getStackInSlot(i);
                        if (stack.getItem() == req.getItem() && stack.getCount() >= req.getCount()) {
                            return true;
                        }
                    }
                    return false;
                });
    }

    private void craft(CompanionWorkBenchRecipe recipe) {
        // Consume inputs
        for (int i = 0; i < inputHandler.getSlots(); i++) {
            inputHandler.getStackInSlot(i).shrink(1);
        }

        // Consume additional costs
        recipe.getAdditionalCosts().forEach(req -> {
            for (int i = 0; i < inputHandler.getSlots(); i++) {
                ItemStack stack = inputHandler.getStackInSlot(i);
                if (stack.getItem() == req.getItem()) {
                    stack.shrink(req.getCount());
                    break;
                }
            }
        });

        // Place output
        for (int i = 0; i < outputHandler.getSlots(); i++) {
            if (outputHandler.getStackInSlot(i).isEmpty()) {
                outputHandler.setStackInSlot(i, recipe.assemble(this));
                break;
            }
        }
    }

    private boolean hasSpaceForOutput() {
        for (int i = 0; i < outputHandler.getSlots(); i++) {
            if (outputHandler.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return side == null || side == Direction.DOWN ?
                    outputOptional.cast() : inputOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inputHandler.deserializeNBT(tag.getCompound("Input"));
        outputHandler.deserializeNBT(tag.getCompound("Output"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Input", inputHandler.serializeNBT());
        tag.put("Output", outputHandler.serializeNBT());
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return null;
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return null;
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {

    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }

    @Override
    public void clearContent() {

    }

    public IItemHandler getInputHandler() {
        return null;
    }

    public IItemHandler getOutputHandler() {
        return null;
    }

    public void drops() {
    }
}