package com.github.mrbestcreator.magicalmechanics.content.block.machine.frame;

import com.github.mrbestcreator.magicalmechanics.content.block.ModBlockEntities;
import com.github.mrbestcreator.magicalmechanics.content.menu.block.machine.frame.FrameBlockMenu;
import com.github.mrbestcreator.magicalmechanics.content.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MachineFrameBlock extends TransparentBlock implements EntityBlock {
    
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    
    public MachineFrameBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    
    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new MachineFrameBlockEntity(blockPos, blockState);
    }
    
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return blockEntityType == ModBlockEntities.MACHINE_FRAME.get()
                ? (lvl, pos, st, be) -> ((MachineFrameBlockEntity) be).tick(lvl, pos, st)
                : null;
    }
    
    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack itemStack, @NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }
        
        if (itemStack.is(ModTags.Items.WRENCH_ITEMS)) {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
        
        BlockEntity be = level.getBlockEntity(blockPos);
        if (be instanceof MachineFrameBlockEntity FBe) {
            player.openMenu(new SimpleMenuProvider(
                    (id, inv, player1) -> new FrameBlockMenu(id, inv, FBe),
                    Component.literal("Frame")
            ), blockPos); // ここでGUIが開く
        }
        
        return ItemInteractionResult.CONSUME;
    }
    
    @Override
    protected void onRemove(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState newBlockState, boolean isMoving) {
        if (!blockState.is(newBlockState.getBlock())) {
            BlockEntity be = level.getBlockEntity(blockPos);
            if (be instanceof MachineFrameBlockEntity machineFrameBlockEntity) {
                for (ItemStack itemStack: machineFrameBlockEntity.getParts()) {
                    if (!itemStack.isEmpty()) {
                        Containers.dropItemStack(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), itemStack);
                    }
                }
                machineFrameBlockEntity.onRemove();
            }
            super.onRemove(blockState, level, blockPos, newBlockState, isMoving);
        }
    }
    
    @Override
    protected boolean skipRendering(@NotNull BlockState state, @NotNull BlockState adjacentBlockState, @NotNull Direction side) {
        return false;
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // プレイヤーの向いている方向の逆にセット（正面を向かせる）
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
}
