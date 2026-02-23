package com.github.mrbestcreator.magicalmechanics.content.block.machine.frame;

import com.github.mrbestcreator.magicalmechanics.content.block.ModBlockEntities;
import com.github.mrbestcreator.magicalmechanics.content.menu.block.machine.frame.FrameBlockMenu;
import com.github.mrbestcreator.magicalmechanics.content.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FrameBlock extends TransparentBlock implements EntityBlock {
    public FrameBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new  FrameBlockEntity(blockPos, blockState);
    }
    
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return blockEntityType == ModBlockEntities.MACHINE_FRAME.get()
                ? (lvl, pos, st, be) -> ((FrameBlockEntity) be).tick(lvl, pos, st)
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
        if (be instanceof FrameBlockEntity FBe) {
            player.openMenu(new SimpleMenuProvider(
                    (id, inv, player1) -> new FrameBlockMenu(id, inv, FBe),
                    Component.literal("Frame")
            ), blockPos); // ここでGUIが開く
        }
        
        return ItemInteractionResult.CONSUME;
    }
    
    @Override
    protected boolean skipRendering(@NotNull BlockState state, @NotNull BlockState adjacentBlockState, @NotNull Direction side) {
        return false;
    }
}
