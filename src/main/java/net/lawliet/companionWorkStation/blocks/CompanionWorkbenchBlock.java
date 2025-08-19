package net.lawliet.companionWorkStation.blocks;

import iskallia.vault.block.base.InventoryRetainerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import net.lawliet.companionWorkStation.blocks.entity.CompanionWorkBenchBlockEntity;
import net.lawliet.companionWorkStation.blocks.entity.ModBlockEntities;

import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public class CompanionWorkbenchBlock extends Block implements EntityBlock, InventoryRetainerBlock<CompanionWorkBenchBlockEntity> {
    public static final DirectionProperty FACING;
    public static final VoxelShape SHAPE;
    private static final VoxelShape BASE;
    private static final VoxelShape PILLAR;
    private static final VoxelShape TOP;
    private static final VoxelShape EGG;

    public CompanionWorkbenchBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        this.addInventoryTooltip(stack, tooltip, CompanionWorkBenchBlockEntity::addInventoryTooltip);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState p_60578_, BlockGetter p_60579_, BlockPos p_60580_) {
        return SHAPE;
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if ((level.getBlockEntity(pos) instanceof CompanionWorkBenchBlockEntity be) && (player instanceof ServerPlayer serverPlayer)) {
            NetworkHooks.openGui(serverPlayer, be, buf -> buf.writeBlockPos(pos));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        this.onInventoryBlockDestroy(level, pos);
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState newState, @Nullable LivingEntity placer, ItemStack stack) {
        this.onInventoryBlockPlace(level, pos, stack);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState p_60576_) {
        return true;
    }

    @Override
    public boolean isPathfindable(BlockState p_60475_, BlockGetter p_60476_, BlockPos p_60477_, PathComputationType p_60478_) {
        return false;
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.COMPANION_WORKBENCH_ENTITY.get().create(pos, state);
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
        ItemStack stack = player.getInventory().getSelected();
        return stack.canPerformAction(net.minecraftforge.common.ToolActions.PICKAXE_DIG);
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        BASE = Block.box(0, 0, 0, 16, 2, 16);
        PILLAR = Block.box(4, 2, 4, 12, 14, 12);
        TOP = Block.box(2, 12, 1, 14, 16, 15);
        EGG = Block.box(6, 15, 6, 10, 20, 10);
        SHAPE = Shapes.join(Stream.of(BASE, PILLAR, TOP).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), EGG, BooleanOp.OR);
    }

}