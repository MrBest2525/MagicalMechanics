package com.github.mrbestcreator.magicalmechanics.content.block.machine.frame;

import com.github.mrbestcreator.magicalmechanics.content.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FrameBlock extends Block implements EntityBlock {
    public FrameBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new  FrameBlockEntity(blockPos, blockState);
    }
    
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide) return null; // サーバー側は無視
        return blockEntityType == ModBlockEntities.MACHINE_FRAME.get()
                ? (lvl, pos, st, be) -> ((FrameBlockEntity) be).tick()
                : null;
    }
    
//    @Override
//    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack itemStack, @NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
//        if (level.isClientSide) return ItemInteractionResult.SUCCESS;
//
//
//        if (!(level.getBlockEntity(blockPos) instanceof FrameBlockEntity be)) {
//            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
//        }
//
//        if (!itemStack.is(ModItems.WRENCH)) return ItemInteractionResult.SUCCESS;
//
//        if (player.getOffhandItem().isEmpty()) return ItemInteractionResult.SUCCESS;
//
//        FrameSlot slot = FrameSlot.fromDirection(blockHitResult.getDirection());
//
//        boolean inserted = be.tryInsert(slot, player.getOffhandItem());
//
//        return ItemInteractionResult.SUCCESS;
//    }
}
