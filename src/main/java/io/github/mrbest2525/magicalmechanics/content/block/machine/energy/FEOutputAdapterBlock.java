package io.github.mrbest2525.magicalmechanics.content.block.machine.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FEOutputAdapterBlock extends Block implements EntityBlock {
    public FEOutputAdapterBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new FEOutputAdapterBlockEntity(blockPos, blockState);
    }
}
