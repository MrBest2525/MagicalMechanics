package io.github.mrbest2525.magicalmechanics.content.block.machine.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class FEInputAdapterBlock extends Block implements EntityBlock {
    public FEInputAdapterBlock(Properties properties) {
        super(properties);
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new FEInputAdapterBlockEntity(pos, state);
    }
    
    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            // ブロックが壊された時に BE を破棄する標準的な処理
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}
