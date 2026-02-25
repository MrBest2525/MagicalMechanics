package com.github.mrbestcreator.magicalmechanics.content.menu.block.machine.frame;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import com.github.mrbestcreator.magicalmechanics.content.block.machine.frame.FrameSlot;
import com.github.mrbestcreator.magicalmechanics.content.menu.util.GuiLayout;
import com.github.mrbestcreator.magicalmechanics.network.packet.menu.OpenFramePartPayload;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class FrameBlockScreen extends AbstractContainerScreen<FrameBlockMenu> {
    
    private final GuiLayout GUI_LAYOUT = new GuiLayout(this.width, this.height);
    
    ResourceLocation PARTS_SLOT_TEXTURE = ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "textures/gui/block/frame/slot.png");
    
    long startTime = System.currentTimeMillis();
    long targetTimeLag = 1000;
    
    // 必要な値の初期化
    float centerX;
    float centerY;
    long elapsed;
    float rawProgress;
    float progress;
    
    
    // パーツのスロット描画
    long time;
    int texterSide;
    float rotation;
    float insideScale;
    float outsideScale;
    float frameInsideRotation;
    float frameMiddlesideRotation;
    float frameOutsideRotation;
    float frameInsideScale;
    float frameMiddlesideScale;
    float frameOutsideScale;
    float frameInsideScaleMultiplier;
    float frameMiddlesideScaleMultiplier;
    float frameOutSideScaleMultiplier;
    
    float sideTargetX;
    float sideTargetY;
    float coreTargetX;
    float coreTargetY;
    float frameTargetX;
    float frameTargetY;
    
    float sideXPos;
    float sideYPos;
    float coreXPos;
    float coreYPos;
    float frameXPos;
    float frameYPos;
    
    // 3D Item & Blockの表示
    float sideItemScale;
    float coreItemScale;
    float frameBlockScale;
    int partsHitBox;
    int frameHitBox;
    
    public FrameBlockScreen(FrameBlockMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        
        // 必要な値の初期化
        centerX = this.width / 2f;
        centerY = this.height / 2f;
        elapsed = System.currentTimeMillis() - startTime;
        rawProgress = Math.min(1.0f, elapsed / (float) targetTimeLag);
        progress = (float) (1.0 - Math.pow(1.0 - rawProgress, 3));
        
        
        // パーツのスロット描画
        time = System.currentTimeMillis();
        texterSide = 32;
        rotation = (time % 4000) / 4000f * 360f;
        insideScale = (float) GUI_LAYOUT.getScale(1.3f);
        outsideScale = (float) GUI_LAYOUT.getScale(1.6f);
        frameInsideRotation = (time % 4000) / 4000f * 360f;
        frameMiddlesideRotation = (((time) % 4000) / 4000f * 360f) * -1;
        frameOutsideRotation = ((time + 500) % 4000) / 4000f * 360f;
        frameInsideScale = (float) GUI_LAYOUT.getScale(2f);
        frameMiddlesideScale = (float) GUI_LAYOUT.getScale(2.3f);
        frameOutsideScale = (float) GUI_LAYOUT.getScale(2.6f);
        frameInsideScaleMultiplier = (float) Math.sin(time * 2 * Math.PI / 2000.0) / 3;
        frameMiddlesideScaleMultiplier = (float) Math.sin((time + 200) * 2 * Math.PI / 2000.0) / 3;
        frameOutSideScaleMultiplier = (float) Math.sin((time + 600) * 2 * Math.PI / 2000.0) / 3;
        
        sideTargetX = GUI_LAYOUT.getPointX(0.3);
        sideTargetY = GUI_LAYOUT.getPointY(0.7);
        coreTargetX = GUI_LAYOUT.getPointX(0.7);
        coreTargetY = GUI_LAYOUT.getPointY(0.7);
        frameTargetX = GUI_LAYOUT.getPointX(0.5);
        frameTargetY = GUI_LAYOUT.getPointY(0.3);
        
        sideXPos = centerX + (sideTargetX - centerX) * progress;
        sideYPos = centerY + (sideTargetY - centerY) * progress;
        coreXPos = centerX + (coreTargetX - centerX) * progress;
        coreYPos = centerY + (coreTargetY - centerY) * progress;
        frameXPos = centerX + (frameTargetX - centerX) * progress;
        frameYPos = centerY + (frameTargetY - centerY) * progress;
        
        // 3D Item & Blockの表示
        sideItemScale = (float) GUI_LAYOUT.getScale(3);
        coreItemScale = (float) GUI_LAYOUT.getScale(3);
        frameBlockScale = (float) GUI_LAYOUT.getScale(30);
        partsHitBox = (int) (16 * (sideItemScale * 10) / 2);
        frameHitBox = (int) (16 * (frameBlockScale * 10) / 2);
    }
    
    @Override
    protected void init() {
        super.init();
        GUI_LAYOUT.updateSize(this.width, this.height);
        
        // 必要な値の初期化
        centerX = this.width / 2f;
        centerY = this.height / 2f;
        elapsed = System.currentTimeMillis() - startTime;
        rawProgress = Math.min(1.0f, elapsed / (float) targetTimeLag);
        progress = (float) (1.0 - Math.pow(1.0 - rawProgress, 3));
        
        
        // パーツのスロット描画
        time = System.currentTimeMillis();
        texterSide = 32;
        rotation = (time % 4000) / 4000f * 360f;
        insideScale = (float) GUI_LAYOUT.getScale(1.3f);
        outsideScale = (float) GUI_LAYOUT.getScale(1.6f);
        frameInsideRotation = (time % 4000) / 4000f * 360f;
        frameMiddlesideRotation = (((time) % 4000) / 4000f * 360f) * -1;
        frameOutsideRotation = ((time + 500) % 4000) / 4000f * 360f;
        frameInsideScale = (float) GUI_LAYOUT.getScale(2f);
        frameMiddlesideScale = (float) GUI_LAYOUT.getScale(2.3f);
        frameOutsideScale = (float) GUI_LAYOUT.getScale(2.6f);
        frameInsideScaleMultiplier = (float) Math.sin(time * 2 * Math.PI / 2000.0) / 3;
        frameMiddlesideScaleMultiplier = (float) Math.sin((time + 200) * 2 * Math.PI / 2000.0) / 3;
        frameOutSideScaleMultiplier = (float) Math.sin((time + 600) * 2 * Math.PI / 2000.0) / 3;
        
        sideTargetX = GUI_LAYOUT.getPointX(0.3);
        sideTargetY = GUI_LAYOUT.getPointY(0.7);
        coreTargetX = GUI_LAYOUT.getPointX(0.7);
        coreTargetY = GUI_LAYOUT.getPointY(0.7);
        frameTargetX = GUI_LAYOUT.getPointX(0.5);
        frameTargetY = GUI_LAYOUT.getPointY(0.3);
        
        sideXPos = centerX + (sideTargetX - centerX) * progress;
        sideYPos = centerY + (sideTargetY - centerY) * progress;
        coreXPos = centerX + (coreTargetX - centerX) * progress;
        coreYPos = centerY + (coreTargetY - centerY) * progress;
        frameXPos = centerX + (frameTargetX - centerX) * progress;
        frameYPos = centerY + (frameTargetY - centerY) * progress;
        
        // 3D Item & Blockの表示
        sideItemScale = (float) GUI_LAYOUT.getScale(3);
        coreItemScale = (float) GUI_LAYOUT.getScale(3);
        frameBlockScale = (float) GUI_LAYOUT.getScale(30);
        partsHitBox = (int) (16 * (sideItemScale * 10) / 2);
        frameHitBox = (int) (16 * (frameBlockScale * 10) / 2);
        System.out.println("sideItemScale: " + sideItemScale);
    }
    
    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderables.forEach(r -> r.render(guiGraphics, mouseX, mouseY, partialTick));
        this.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        
    }
    
    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        
        // 必要な値の初期化
        centerX = this.width / 2f;
        centerY = this.height / 2f;
        elapsed = System.currentTimeMillis() - startTime;
        rawProgress = Math.min(1.0f, elapsed / (float) targetTimeLag);
        progress = (float) (1.0 - Math.pow(1.0 - rawProgress, 3));
        
        
        // パーツのスロット描画
        time = System.currentTimeMillis();
//        texterSide = 32;
        rotation = (time % 4000) / 4000f * 360f;
        insideScale = (float) GUI_LAYOUT.getScale(1.3f);
        outsideScale = (float) GUI_LAYOUT.getScale(1.6f);
        frameInsideRotation = (time % 4000) / 4000f * 360f;
        frameMiddlesideRotation = (((time) % 4000) / 4000f * 360f) * -1;
        frameOutsideRotation = ((time + 500) % 4000) / 4000f * 360f;
        frameInsideScale = (float) GUI_LAYOUT.getScale(2f);
        frameMiddlesideScale = (float) GUI_LAYOUT.getScale(2.3f);
        frameOutsideScale = (float) GUI_LAYOUT.getScale(2.6f);
        frameInsideScaleMultiplier = (float) Math.sin(time * 2 * Math.PI / 2000.0) / 3;
        frameMiddlesideScaleMultiplier =(float) Math.sin((time + 200) * 2 * Math.PI / 2000.0) / 3;
        frameOutSideScaleMultiplier =(float) Math.sin((time + 600) * 2 * Math.PI / 2000.0) / 3;
        
        sideTargetX = GUI_LAYOUT.getPointX(0.3);
        sideTargetY = GUI_LAYOUT.getPointY(0.7);
        coreTargetX = GUI_LAYOUT.getPointX(0.7);
        coreTargetY = GUI_LAYOUT.getPointY(0.7);
        frameTargetX = GUI_LAYOUT.getPointX(0.5);
        frameTargetY = GUI_LAYOUT.getPointY(0.3);
        
        // progressをかけて出現アニメーションの作成
        insideScale *= progress;
        outsideScale  *= progress;
        frameInsideScale *= progress;
        frameMiddlesideScale *= progress;
        frameOutsideScale *= progress;
        
        sideXPos = centerX + (sideTargetX - centerX) * progress;
        sideYPos = centerY + (sideTargetY - centerY) * progress;
        coreXPos = centerX + (coreTargetX - centerX) * progress;
        coreYPos = centerY + (coreTargetY - centerY) * progress;
        frameXPos = centerX + (frameTargetX - centerX) * progress;
        frameYPos = centerY + (frameTargetY - centerY) * progress;
        
        // Side, CoreのPartsスロット
        
        // Side
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(sideXPos, sideYPos, 0);
        guiGraphics.pose().scale(insideScale, insideScale, insideScale);
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(rotation));
        guiGraphics.pose().translate(-(texterSide / 2f), -(texterSide / 2f), 0);
        guiGraphics.blit(PARTS_SLOT_TEXTURE, 0, 0, 0, 0, texterSide, texterSide, texterSide, texterSide);
        guiGraphics.pose().popPose();
        
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(sideXPos, sideYPos, 0);
        guiGraphics.pose().scale(outsideScale, outsideScale, outsideScale);
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(rotation * -1));
        guiGraphics.pose().translate(-(texterSide / 2f), -(texterSide / 2f), 0);
        guiGraphics.blit(PARTS_SLOT_TEXTURE, 0, 0, 0, 0, texterSide, texterSide, texterSide, texterSide);
        guiGraphics.pose().popPose();
        
        // Core
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(coreXPos, coreYPos, 0);
        guiGraphics.pose().scale(insideScale, insideScale, insideScale);
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(rotation));
        guiGraphics.pose().translate(-(texterSide / 2f), -(texterSide / 2f), 0);
        guiGraphics.blit(PARTS_SLOT_TEXTURE, 0, 0, 0, 0, texterSide, texterSide, texterSide, texterSide);
        guiGraphics.pose().popPose();
        
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(coreXPos, coreYPos, 0);
        guiGraphics.pose().scale(outsideScale, outsideScale, outsideScale);
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(rotation * -1));
        guiGraphics.pose().translate(-(texterSide / 2f), -(texterSide / 2f), 0);
        guiGraphics.blit(PARTS_SLOT_TEXTURE, 0, 0, 0, 0, texterSide, texterSide, texterSide, texterSide);
        guiGraphics.pose().popPose();
        
        
        // Frame自体のスロット
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(frameXPos, frameYPos, 0);
        guiGraphics.pose().scale(frameInsideScale + frameInsideScaleMultiplier, frameInsideScale + frameInsideScaleMultiplier, frameInsideScale + frameInsideScaleMultiplier);
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(frameInsideRotation));
        guiGraphics.pose().translate(-(texterSide / 2f), -(texterSide / 2f), 0);
        guiGraphics.blit(PARTS_SLOT_TEXTURE, 0, 0, 0, 0, texterSide, texterSide, texterSide, texterSide);
        guiGraphics.pose().popPose();
        
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(frameXPos, frameYPos, 0);
        guiGraphics.pose().scale(frameMiddlesideScale - frameMiddlesideScaleMultiplier, frameMiddlesideScale - frameMiddlesideScaleMultiplier, frameMiddlesideScale - frameMiddlesideScaleMultiplier);
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(frameMiddlesideRotation));
        guiGraphics.pose().translate(-(texterSide / 2f), -(texterSide / 2f), 0);
        guiGraphics.blit(PARTS_SLOT_TEXTURE, 0, 0, 0, 0, texterSide, texterSide, texterSide, texterSide);
        guiGraphics.pose().popPose();
        
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(frameXPos, frameYPos, 0);
        guiGraphics.pose().scale(frameOutsideScale + frameOutSideScaleMultiplier, frameOutsideScale + frameOutSideScaleMultiplier, frameOutsideScale + frameOutSideScaleMultiplier);
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(frameOutsideRotation));
        guiGraphics.pose().translate(-(texterSide / 2f), -(texterSide / 2f), 0);
        guiGraphics.blit(PARTS_SLOT_TEXTURE, 0, 0, 0, 0, texterSide, texterSide, texterSide, texterSide);
        guiGraphics.pose().popPose();
        
        
        // 3D Item & Blockの表示
        sideItemScale = isSideHovered(mouseX, mouseY) ? (float) GUI_LAYOUT.getScale(4) : (float) GUI_LAYOUT.getScale(3);
        coreItemScale = isCoreHovered(mouseX, mouseY) ? (float) GUI_LAYOUT.getScale(4) : (float) GUI_LAYOUT.getScale(3);
        frameBlockScale = isFrameHovered(mouseX, mouseY) ? (float) GUI_LAYOUT.getScale(40) : (float) GUI_LAYOUT.getScale(30);
        
        // HitBox を現在のスケールに合わせて更新
        partsHitBox = (int)(8 * Math.max(sideItemScale, coreItemScale));
        frameHitBox = (int)(8 * Math.max(sideItemScale, coreItemScale));
        
        // progressをかけて出現アニメーションの作成
        sideItemScale *= progress;
        coreItemScale *= progress;
        frameBlockScale *= progress;
        
        // Partsの表示
        
        // Side
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(sideXPos, sideYPos, 1);
        guiGraphics.pose().scale(sideItemScale, sideItemScale, sideItemScale);
        guiGraphics.pose().translate(-8, -8, 0);
        guiGraphics.renderFakeItem(this.menu.blockEntity.getPart(FrameSlot.SIDE), 0, 0);
        guiGraphics.pose().popPose();
        
        // Core
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(coreXPos, coreYPos, 1);
        guiGraphics.pose().scale(coreItemScale, coreItemScale, coreItemScale);
        guiGraphics.pose().translate(-8, -8, 0);
        guiGraphics.renderFakeItem(this.menu.blockEntity.getPart(FrameSlot.CORE), 0, 0);
        guiGraphics.pose().popPose();
        
        
        // Franeの表示
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(frameXPos - frameBlockScale / 2, frameYPos + frameBlockScale / 2, 1);
        guiGraphics.pose().scale(frameBlockScale, -frameBlockScale, frameBlockScale);
        guiGraphics.pose().translate(0.5, 0.5, 0.5);
        guiGraphics.pose().mulPose(Axis.XP.rotationDegrees(30));
        guiGraphics.pose().mulPose(Axis.YP.rotationDegrees(225));
        guiGraphics.pose().translate(-0.5, -0.5, -0.5);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(this.menu.blockEntity.getBlockState(), guiGraphics.pose(), guiGraphics.bufferSource(), 15728880, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);
        guiGraphics.pose().popPose();
    }
    
    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
//        super.renderLabels(guiGraphics, mouseX, mouseY);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int id = -1;
            if (isSideHovered(mouseX, mouseY)) id = 0;
            else if (isCoreHovered(mouseX, mouseY)) id = 1;
            else if (isFrameHovered(mouseX, mouseY)) id = 2;
            
            if (id != -1) {
                // サーバーへパケット送信
                PacketDistributor.sendToServer(new OpenFramePartPayload(this.menu.blockEntity.getBlockPos(), id));
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    private boolean isSideHovered(double mouseX, double mouseY) {
        return mouseX >= (sideXPos - partsHitBox) && mouseX <= (sideXPos + partsHitBox)
                && mouseY >= (sideYPos - partsHitBox) && mouseY <= (sideYPos + partsHitBox);
    }
    
    private boolean isCoreHovered(double mouseX, double mouseY){
        return mouseX >= (coreXPos - partsHitBox) && mouseX <= (coreXPos + partsHitBox)
                && mouseY >= (coreYPos - partsHitBox) && mouseY <= (coreYPos + partsHitBox);
    }
    
    private boolean isFrameHovered(double mouseX, double mouseY){
        return mouseX >= (frameXPos - frameHitBox) && mouseX <= (frameXPos + frameHitBox)
                && mouseY >= (frameYPos - frameHitBox) && mouseY <= (frameYPos + frameHitBox);
    }
}
