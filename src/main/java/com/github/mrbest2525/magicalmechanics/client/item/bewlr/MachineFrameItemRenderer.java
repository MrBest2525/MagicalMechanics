package com.github.mrbest2525.magicalmechanics.client.item.bewlr;

import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameSlot;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

public class MachineFrameItemRenderer extends BlockEntityWithoutLevelRenderer {
    
    private final BlockEntityRenderDispatcher dispatcher;
    private MachineFrameBlockEntity dummyBE;
    
    public MachineFrameItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
        this.dispatcher = dispatcher;
    }
    
    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext context, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        
        BlockState state = Block.byItem(stack.getItem()).defaultBlockState();
        
        // 1. 外枠（フレーム本体）の描画
        // これを入れないと、中身のコアだけが浮いている状態になります
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
                state,
                poseStack,
                buffer,
                combinedLight,
                combinedOverlay,
                ModelData.EMPTY,
                null
        );
        
        // 1. 描画時に初めて生成する (この時点なら Map も Block も確実に存在する)
        if (this.dummyBE == null) {
            Block block = Block.byItem(stack.getItem());
            // 万が一 AIR だったら描画をスキップしてクラッシュを防ぐ
            if (block == Blocks.AIR) return;
            
            this.dummyBE = new MachineFrameBlockEntity(BlockPos.ZERO, block.defaultBlockState());
            this.dummyBE.setLevel(Minecraft.getInstance().level);
        }
        
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
                state, poseStack, buffer, combinedLight, combinedOverlay, ModelData.EMPTY, null
        );
        
        // 2. 状態の更新
        this.resetDummyBE();
        this.dummyBE.setBlockState(Block.byItem(stack.getItem()).defaultBlockState());
        
        // 3. NBTデータのロード
        CustomData customData = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (customData != null && this.dummyBE.getLevel() != null) {
            customData.loadInto(this.dummyBE, this.dummyBE.getLevel().registryAccess());
        }
        
        // 4. 描画
        this.dispatcher.renderItem(this.dummyBE, poseStack, buffer, combinedLight, combinedOverlay);
    }
    
    private void resetDummyBE() {
        // BlockEntity側の実装に合わせて、内部変数をリセットしてください
        // 例: this.dummyBE.setCoreStack(ItemStack.EMPTY);
        this.dummyBE.setPart(MachineFrameSlot.CORE, ItemStack.EMPTY);
        this.dummyBE.setPart(MachineFrameSlot.SIDE, ItemStack.EMPTY);
    }
}
