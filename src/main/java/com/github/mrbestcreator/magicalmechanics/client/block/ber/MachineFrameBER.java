package com.github.mrbestcreator.magicalmechanics.client.block.ber;

import com.github.mrbestcreator.magicalmechanics.content.block.machine.frame.MachineFrameSlot;
import com.github.mrbestcreator.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MachineFrameBER implements BlockEntityRenderer<MachineFrameBlockEntity> {
    private final ItemRenderer itemRenderer;
    
    public MachineFrameBER(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }
    
    @Override
    public void render(@NotNull MachineFrameBlockEntity machineFrameBlockEntity, float v, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay) {
        // COREの表示
        ItemStack coreStack = machineFrameBlockEntity.getPart(MachineFrameSlot.CORE);
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.scale(2, 2, 2);
        poseStack.scale(0.998f, 0.998f, 0.998f);
        this.itemRenderer.renderStatic(coreStack, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, multiBufferSource, machineFrameBlockEntity.getLevel(), 0);
        poseStack.popPose();
        
        // SIDEの表示
        ItemStack sideStack = machineFrameBlockEntity.getPart(MachineFrameSlot.SIDE);
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.scale(2, 2, 2);
        poseStack.scale(0.998f, 0.998f, 0.998f);
        this.itemRenderer.renderStatic(sideStack, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, multiBufferSource, machineFrameBlockEntity.getLevel(), 0);
        poseStack.popPose();
    }
}
