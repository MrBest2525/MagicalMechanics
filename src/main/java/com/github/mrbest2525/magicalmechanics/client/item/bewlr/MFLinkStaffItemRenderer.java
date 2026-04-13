package com.github.mrbest2525.magicalmechanics.client.item.bewlr;

import com.github.mrbest2525.magicalmechanics.MagicalMechanics;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class MFLinkStaffItemRenderer extends BlockEntityWithoutLevelRenderer {
    
    // BakedModel を保持する
    private BakedModel baseModel;
    private BakedModel orbModel;
    private BakedModel auraModel;
    
    public MFLinkStaffItemRenderer(BlockEntityRenderDispatcher rd, EntityModelSet ems) {
        super(rd, ems);
    }
    
    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext context, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        // モデルが未ロードならここで取得（一度だけ実行される）
        if (baseModel == null) {
            ModelManager manager = Minecraft.getInstance().getModelManager();
            this.baseModel = manager.getModel(new ModelResourceLocation(
                    ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "item/mf_link_staff_base"), "standalone"));
            
            this.orbModel = manager.getModel(new ModelResourceLocation(
                    ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "item/mf_link_staff_orb_core"), "standalone"));
            
            this.auraModel = manager.getModel(new ModelResourceLocation(
                    ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "item/mf_link_staff_orb_aura"), "standalone"));
        }
        
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        
        boolean isLeftHand = context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND
                || context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        
        poseStack.pushPose();
        
        poseStack.translate(0.5, 0.5, 0.5);
        
        // 杖本体の描画
        renderer.render(
                stack,
                context,
                isLeftHand, // 左手持ちフラグ
                poseStack,
                buffer,
                combinedLight,
                combinedOverlay,
                baseModel // ここに直接モデルを渡す
        );
        
        poseStack.pushPose();
        
        // orb_core
        renderOrb(
                renderer,
                stack,
                context,
                isLeftHand,
                poseStack,
                buffer,
                combinedLight,
                combinedOverlay,
                orbModel,
                false
        );
        renderOrb(
                renderer,
                stack,
                context,
                isLeftHand,
                poseStack,
                buffer,
                combinedLight,
                combinedOverlay,
                orbModel,
                true
        );
        
        // orb_aura
        renderBilBord(
                renderer,
                stack,
                context,
                isLeftHand,
                poseStack,
                buffer,
                combinedLight,
                combinedOverlay,
                auraModel
        );
        
        poseStack.popPose();
        
        poseStack.popPose();
    }
    
    private void renderOrb(ItemRenderer renderer, @NotNull ItemStack stack, ItemDisplayContext context, boolean isLeftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model, boolean isSecund) {
        if (model == null) return;
        
        poseStack.pushPose();
        
        int seed = stack.hashCode();
        float speedMultiplier = 0.5F + (Math.abs(seed % 100) / 100.0F);
        float direction = (seed % 2 == 0) ? 1.0F : -1.0F;
        float side = isLeftHand ? -1.0F : 1.0F;
        
        // --- 時間の計算 (longの精度を維持) ---
        long currentTime = System.currentTimeMillis();
        long personalOffset = (long)(Math.abs(seed % 1000) * 123);
        
        // 1000msで1周をベースに、個別の速度倍率を適用した「経過秒数」を出す
        double totalSeconds = ((currentTime + personalOffset) / 1000.0) * speedMultiplier;
        
        // --- ガクつかない回転の計算 ---
        // ここで直接「余り」を計算します。
        // rawProgressは 0.0 ~ 1.0 の間を滑らかに動き、1.0の次は必ず0.0に戻ります。
        double rawProgress = totalSeconds % 1.0;
        
        // サイン波で緩急をつける (0.0と1.0では必ず0になるので、つなぎ目が完璧に消える)
        double curve = Math.sin(rawProgress * 6.283185307179586) * 0.05;
        
        // 0~360度の最終的な角度
        float finalRotation = (float)((rawProgress + curve) * 360.0) * direction * side;
        
        // 上下の浮遊
        float yOffset = (float) Math.sin(totalSeconds * 2.0) * 0.05F;
        poseStack.translate(0, 0.7 + yOffset, 0);
        
        // 多軸回転
        if (!isSecund) {
            // Y軸メインの回転 + Z軸の揺れ
            poseStack.mulPose(Axis.YP.rotationDegrees(finalRotation));
            float wobble = (float) Math.cos(totalSeconds * 1.5) * 10.0F;
            poseStack.mulPose(Axis.ZP.rotationDegrees(wobble));
        } else {
            poseStack.mulPose(Axis.XP.rotationDegrees(45));
            poseStack.mulPose(Axis.ZP.rotationDegrees(45));
            // 逆回転させて複雑さを出す
            poseStack.mulPose(Axis.YP.rotationDegrees(-finalRotation * 1.2F));
        }
        
        // Z軸の揺れ
        float wobble = (float) Math.cos(totalSeconds * 1.5) * 10.0F;
        poseStack.mulPose(Axis.ZP.rotationDegrees(wobble));
        
        
        renderer.render(
                stack,
                context,
                isLeftHand,
                poseStack,
                buffer,
                combinedLight,
                combinedOverlay,
                model
        );
        
        poseStack.popPose();
    }
    
    private void renderBilBord(ItemRenderer renderer, @NotNull ItemStack stack, ItemDisplayContext context, boolean isLeftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model) {
        poseStack.pushPose();
        
        int seed = stack.hashCode();
        float speedMultiplier = 0.5F + (Math.abs(seed % 100) / 100.0F);
        
        // --- 時間の計算 (longの精度を維持) ---
        long currentTime = System.currentTimeMillis();
        long personalOffset = (long)(Math.abs(seed % 1000) * 123);
        
        // 1000msで1周をベースに、個別の速度倍率を適用した「経過秒数」を出す
        double totalSeconds = ((currentTime + personalOffset) / 1000.0) * speedMultiplier;
        
        poseStack.translate(-0.5, 0, -0.5);
        
        
        // 位置の調整（浮遊など）
        float yOffset = (float) Math.sin(totalSeconds * 2.0) * 0.05F;
        poseStack.translate(0.5, 0.7 + yOffset, 0.5);

        // 回転の上書き
        Matrix4f matrix = poseStack.last().pose();

        matrix.m00(1.0f); matrix.m01(0.0f); matrix.m02(0.0f);
        matrix.m10(0.0f); matrix.m11(1.0f); matrix.m12(0.0f);
        matrix.m20(0.0f); matrix.m21(0.0f); matrix.m22(1.0f);

        // 3. カメラの回転を適用
        Quaternionf cameraOrientation = Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation();
        matrix.rotate(cameraOrientation);
        
        renderer.render(
                stack,
                context,
                isLeftHand,
                poseStack,
                buffer,
                combinedLight,
                combinedOverlay,
                model
        );
        
        poseStack.popPose();
    }
}
