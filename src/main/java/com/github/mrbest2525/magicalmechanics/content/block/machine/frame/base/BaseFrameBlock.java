package com.github.mrbest2525.magicalmechanics.content.block.machine.frame.base;

import com.github.mrbest2525.magicalmechanics.content.block.ModBlocks;
import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;


public class BaseFrameBlock extends Block {
    public BaseFrameBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack itemStack, @NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
        // 手に持っているアイテムがいずれかのティアに該当するかチェック
        MachineFrameTiers matchedTier = null;
        for (MachineFrameTiers tier : MachineFrameTiers.values()) {
            if (itemStack.is(tier.getAssemblyItem())) {
                matchedTier = tier;
                break;
            }
        }
        
        // もし該当するアイテムでなければ、即座に PASS を返す
        // 土などの「関係ないブロック」は通常通り設置されるように
        if (matchedTier == null) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        
        // --- ここからは「組み立てアイテム」だった場合の処理 ---
        
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }
        
        // 対応するブロックを取得
        Block nextBlock = ModBlocks.MACHINE_FRAMES.get(matchedTier.getMachineFrameId()).get();
        
        // ブロックを置き換え
        level.setBlockAndUpdate(blockPos, nextBlock.defaultBlockState());
        
        // 音を鳴らす（正しい形式）
        level.playSound(null, blockPos,
                nextBlock.defaultBlockState().getSoundType(level, blockPos, player).getPlaceSound(),
                SoundSource.BLOCKS, 1.0f, 1.0f);
        
        // アイテム消費
        if (!player.isCreative()) {
            itemStack.shrink(1);
        }
        
        return ItemInteractionResult.CONSUME;
        
    }
}
