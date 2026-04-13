package com.github.mrbest2525.magicalmechanics.client.block.ber;

import com.github.mrbest2525.magicalmechanics.MagicalMechanics;
import com.github.mrbest2525.magicalmechanics.api.ISourceTypeProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class FEAdapterBlockBER<T extends BlockEntity & ISourceTypeProvider> implements BlockEntityRenderer<T> {
    
    private BakedModel orbModel;
    private BakedModel auraModel;
    
    public FEAdapterBlockBER(BlockEntityRendererProvider.Context context) {
    
    }
    
    @Override
    public void render(@NotNull T entity, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (orbModel == null) {
            ModelManager manager = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getModelManager();
            
            this.orbModel = manager.getModel(new ModelResourceLocation(
                    ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "orb/orb_core"), "standalone"));
            
            this.auraModel = manager.getModel(new ModelResourceLocation(
                    ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "orb/orb_aura"), "standalone"));
        }
        
        // --- 滑らかな時間の計算 ---
        // ゲームの総時間に partialTicks を足して、シード値で個別にずらす
        long gameTick = entity.getLevel() != null ? entity.getLevel().getGameTime() : 0;
        float gameTime = gameTick + partialTicks;
        int seed = entity.getBlockPos().hashCode(); // 座標固定のシード
        float personalTime = gameTime + (Math.abs(seed % 1000));
        
        // 速度調整 (ここをいじると回転の速さが変わる)
        float rotationSpeed = 2.0F;
        float currentRotation = personalTime * rotationSpeed;
        
        poseStack.pushPose();
        
        poseStack.translate(0.5, 0.5, 0.5);
        
        // --- Orb Core の描画 ---
        poseStack.pushPose();
        originMulPose(false, poseStack, currentRotation);
        renderModel(poseStack, buffer, combinedLight, combinedOverlay, orbModel, entity);
        poseStack.popPose();
        
        poseStack.pushPose();
        originMulPose(true, poseStack, currentRotation);
        renderModel(poseStack, buffer, combinedLight, combinedOverlay, orbModel, entity);
        poseStack.popPose();
        
        // --- Orb Aura (Billboard) の描画 ---
        poseStack.pushPose();
        // 浮遊アニメーション
        float offset = (float) Math.sin(personalTime * 0.1F) * 0.05F * 2;
        poseStack.scale(1 + offset, 1 + offset, 1 + offset);
        
        // ビルボード処理
        Quaternionf cameraQuat = Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation();
        poseStack.mulPose(cameraQuat);
        
        renderModel(poseStack, buffer, combinedLight, combinedOverlay, auraModel, entity);
        poseStack.popPose();
        
        poseStack.popPose();
    }
    
    private void originMulPose(boolean isSecund, PoseStack poseStack, float rotation) {
        
        if (!isSecund) {
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
            poseStack.mulPose(Axis.ZP.rotationDegrees(rotation));
        } else {
            poseStack.mulPose(Axis.XP.rotationDegrees(45));
            poseStack.mulPose(Axis.ZP.rotationDegrees(45));
            poseStack.mulPose(Axis.YP.rotationDegrees(-rotation * 1.2F));
        }
    }
    
    // 描画ヘルパー
    private void renderModel(PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, BakedModel model, T entity) {
        VertexConsumer consumer = buffer.getBuffer(RenderType.translucent());
        
        int color = entity.getSourceType().getColor();
        
        float a = ((color >> 24) & 0xFF) / 255.0f;
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        
        // アイテムレンダラーを使って、モデルの全クアッド（面）を強制描画
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
                poseStack.last(),
                consumer,
                null,       // BlockState (nullでOK)
                model,
                r, g, b,    // 各成分を渡す
                light,
                overlay,
                ModelData.EMPTY, // NeoForgeのModelData
                RenderType.translucent()        // RenderType (nullならconsumerに従う)
        );
    }
}
