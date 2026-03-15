package com.github.mrbestcreator.magicalmechanics.client.item.bewlr;

import com.github.mrbestcreator.magicalmechanics.content.block.ModBlocks;
import com.github.mrbestcreator.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
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
        // 1. フレーム（外枠）の描画
        // ブロックのモデルをそのままアイテムとして描画する
        BlockState state = ModBlocks.MACHINE_FRAME.get().defaultBlockState();
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
                state, poseStack, buffer, combinedLight, combinedOverlay, ModelData.EMPTY, null
        );
        
        // 2. ダミーBEの初期化
        if (dummyBE == null) {
            dummyBE = new MachineFrameBlockEntity(BlockPos.ZERO, ModBlocks.MACHINE_FRAME.get().defaultBlockState());
        }
        
        // 3. アイテムのコンポーネントからデータを復元
        CustomData data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (data != null) {
            // ここでアイテム内のNBTをダミーBEに流し込む
            data.loadInto(dummyBE, Minecraft.getInstance().level != null ? Minecraft.getInstance().level.registryAccess() : null);
        } else {
            // データがない場合は中身を空にする（重要！）
            dummyBE.clearContent();
        }
        
        // 4. 既存のBERを呼び出して描画
        this.dispatcher.renderItem(dummyBE, poseStack, buffer, combinedLight, combinedOverlay);
    }
}
