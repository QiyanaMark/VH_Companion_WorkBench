package vault_work_station.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import vault_work_station.blocks.entity.CompanionWorkBenchBlockEntity;
import vault_work_station.menu.CompanionWorkBenchMenu;



public class CompanionWorkbenchBlock extends BaseEntityBlock {
    public CompanionWorkbenchBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide() && player instanceof ServerPlayer) {
            MenuProvider provider = new MenuProvider() {
                @Override
                public TranslatableComponent getDisplayName() {
                    return new TranslatableComponent("container.vault_work_station.companion_workbench");
                }

                @Override
                public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
                    return new CompanionWorkBenchMenu(id, inv, pos);
                }
            };
            NetworkHooks.openGui((ServerPlayer)player, provider, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CompanionWorkBenchBlockEntity(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof CompanionWorkBenchBlockEntity) {
                ((CompanionWorkBenchBlockEntity) entity).drops();
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}