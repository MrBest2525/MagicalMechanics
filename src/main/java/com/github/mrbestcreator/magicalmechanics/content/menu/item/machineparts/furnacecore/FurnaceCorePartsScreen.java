package com.github.mrbestcreator.magicalmechanics.content.menu.item.machineparts.furnacecore;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import com.github.mrbestcreator.magicalmechanics.content.menu.util.GuiLayout;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class FurnaceCorePartsScreen extends AbstractContainerScreen<FurnaceCorePartsMenu> {
    
    private final GuiLayout GUI_LAYOUT = new GuiLayout(this.width, this.height);
    
    private final ResourceLocation FIRE_STAND_TEXTURE = ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "textures/gui/item/frame_parts/core/furnace_core/fire_stand.png");
    private final ResourceLocation INVENTORY_SLOT_TEXTURE = ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "textures/gui/inventory_slot/normal_inventory_slot.png");
    
    private final float fireX = 0.5f;
    private final float fireY = 0.3f;
    private final float fireStandDefaultScale = 5;
    private final float fuelSlotScale = 4.4f;
    private final float inventoryX = 0.5f;
    private final float inventoryY = 0.65f;
    private final List<Particles> particlesList = new ArrayList<>();
    
    public FurnaceCorePartsScreen(FurnaceCorePartsMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        for (int i = 0; i < 100; i++) {
            particlesList.add(new Particles(fireX, fireY));
        }
    }
    
    @Override
    protected void init() {
        this.imageWidth = this.width;
        this.imageHeight = this.height;
        super.init();
        GUI_LAYOUT.updateSize(this.width, this.height);
    }
    
    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
//        super.renderables.forEach(r -> r.render(guiGraphics, mouseX, mouseY, partialTick));
//        this.renderBg(guiGraphics, partialTick, mouseX, mouseY);
//        this.renderTooltip(guiGraphics, mouseX, mouseY);
        
    }
    
    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
//        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
//        this.renderTransparentBackground(guiGraphics);
        this.renderBg(guiGraphics, partialTick, mouseX, mouseY);
    }
    
    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float v, int i, int i1) {
        // TODO 四角いパーティクル的なので(■)燃えてる時に炎があがっていくような動的Screenにしたい
        float fireStandScale = (float) GUI_LAYOUT.getScale(this.fireStandDefaultScale);
        
        ContainerData data = this.menu.data;
        
        float minTemp = 0f;
        float maxTemp = 1000f;
        int color = calculateColor(Mth.clamp((Float.intBitsToFloat(data.get(1)) - minTemp) / (maxTemp - minTemp), 0.0f, 1.0f), 255);
        
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(GUI_LAYOUT.getPointX(fireX), GUI_LAYOUT.getPointY(fireY), 0);
        guiGraphics.pose().scale(fireStandScale, fireStandScale, fireStandScale);
        guiGraphics.pose().translate(-32, -32, 0);
        blitWithGradient(guiGraphics, FIRE_STAND_TEXTURE, 0, 0, 0, 0, 64, 64, 64, 64, color);
        guiGraphics.pose().popPose();
        
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(GUI_LAYOUT.getPointX(inventoryX), GUI_LAYOUT.getPointY(inventoryY), 0);
        guiGraphics.pose().translate(-110, -83, 0);
        // 1. ブレンド（透明度）を有効化
        RenderSystem.enableBlend();
        // 2. ブレンド関数の設定（通常はこの設定でOK）
        RenderSystem.defaultBlendFunc();
        // 3. 色を白（1.0, 1.0, 1.0, 1.0）にリセットして透明度を維持
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(INVENTORY_SLOT_TEXTURE, 0, 0, 0, 0, 220, 166, 220, 166);
        // 4. ブレンドを無効化（他の描画への影響を防ぐため）
        RenderSystem.disableBlend();
        guiGraphics.pose().popPose();
        
        
        if (data.get(0) == 1) {
            particlesList.forEach(particles -> particles.render(guiGraphics, Float.intBitsToFloat(data.get(1))));
            
        }
//        guiGraphics.drawString(this.font, "温度: " + Float.intBitsToFloat(data.get(1)) + "℃", GUI_LAYOUT.getPointX(0.5), GUI_LAYOUT.getPointY(0.5), 0xFFFFFFFF, true);
        
    }
    
    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }
    
    @Override
    protected void renderSlot(@NotNull GuiGraphics guiGraphics, @NotNull Slot slot) {
        if (slot.index == 0) {
            float scale = (float) GUI_LAYOUT.getScale(fuelSlotScale);
            float targetX = GUI_LAYOUT.getPointX(fireX);
            float targetY = (float) (GUI_LAYOUT.getPointY(fireY) + 90 * GUI_LAYOUT.getScale(1));
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(targetX, targetY, 0);
            guiGraphics.pose().scale(scale, scale, scale);
            guiGraphics.pose().translate(-8, -8, 0);
            super.renderSlot(guiGraphics, slot);
            guiGraphics.pose().popPose();
        } else if (slot.index >= 1 && slot.index <= 27) {
            float targetX = GUI_LAYOUT.getPointX(inventoryX);
            float targetY = GUI_LAYOUT.getPointY(inventoryY);
            int i = slot.index - 1;
            float offsetX = -80 + (18 * (i % 9));
            float offsetY = -37 + (18 * (int) (i / 9));
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(targetX, targetY, 0);
            guiGraphics.pose().translate(offsetX, offsetY, 0);
            super.renderSlot(guiGraphics, slot);
            guiGraphics.pose().popPose();
        } else if (slot.index >= 28 && slot.index <= 36) {
            float targetX = GUI_LAYOUT.getPointX(inventoryX);
            float targetY = GUI_LAYOUT.getPointY(inventoryY);
            int i = slot.index - 1;
            float offsetX = -80 + (18 * (i % 9));
            float offsetY = -37 + 18 * 3 + 4;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(targetX, targetY, 0);
            guiGraphics.pose().translate(offsetX, offsetY , 0);
            super.renderSlot(guiGraphics, slot);
            guiGraphics.pose().popPose();
        } else {
            super.renderSlot(guiGraphics, slot);
        }
    }
    
    @Override
    public boolean isHovering(@NotNull Slot slot, double mouseX, double mouseY) {
        float finalX, finalY;
        float currentScale = 1.0f;
        
        if (slot.index == 0) {
            // --- ケース1: 燃料スロット (Scaleあり) ---
            currentScale = (float) GUI_LAYOUT.getScale(fuelSlotScale);
            finalX = GUI_LAYOUT.getPointX(fireX);
            finalY = (float) (GUI_LAYOUT.getPointY(fireY) + 90 * GUI_LAYOUT.getScale(1));
            
        } else if (slot.index >= 1 && slot.index <= 27) {
            // --- ケース2: インベントリメイン ---
            float targetX = GUI_LAYOUT.getPointX(inventoryX);
            float targetY = GUI_LAYOUT.getPointY(inventoryY);
            int i = slot.index - 1;
            finalX = targetX + (-80 + (18 * (i % 9))) + 8;
            finalY = targetY + (-37 + (18 * (int) (i / 9))) + 8;
            
        } else if (slot.index >= 28 && slot.index <= 36) {
            // --- ケース3: ホットバー ---
            float targetX = GUI_LAYOUT.getPointX(inventoryX);
            float targetY = GUI_LAYOUT.getPointY(inventoryY);
            int i = slot.index - 1;
            finalX = targetX + (-80 + (18 * (i % 9))) + 8;
            finalY = targetY + (-37 + 18 * 3 + 4) + 8;
            
        } else {
            // その他（デフォルト）
            return super.isHovering(slot.x, slot.y, 16, 16, mouseX, mouseY);
        }
        
        // 判定範囲の計算 (中心 finalX, finalY から、サイズ 16 * scale の半分ずつ広げる)
        float halfSize = (16.0f * currentScale) / 2.0f;
        
//        return mouseX >= (double)finalX - halfSize && mouseX <= (double)finalX + halfSize &&
//                mouseX >= (double)finalY - halfSize && mouseX <= (double)finalY + halfSize;
        return  this.isHovering((int) (finalX - halfSize), (int) (finalY - halfSize), (int) (halfSize * 2), (int) (halfSize * 2), mouseX, mouseY);
    }
    
    @Override
    protected void renderSlotHighlight(@NotNull GuiGraphics guiGraphics, @NotNull Slot slot, int mouseX, int mouseY, float partialTick) {
        // 自分の判定ロジックが true のときだけ描画
        if (this.isHovering(slot, (double)mouseX, (double)mouseY)) {
            
            // --- ここで isHovering と同じ finalX, finalY, currentScale を算出 ---
            // (コードの重複を避けるなら、座標算出部分をメソッドに切り出すと楽です)
            float finalX, finalY;
            float currentScale = 1.0f;
            
            if (slot.index == 0) {
                // --- ケース1: 燃料スロット (Scaleあり) ---
                currentScale = (float) GUI_LAYOUT.getScale(fuelSlotScale);
                finalX = GUI_LAYOUT.getPointX(fireX);
                finalY = (float) (GUI_LAYOUT.getPointY(fireY) + 90 * GUI_LAYOUT.getScale(1));
                
            } else if (slot.index >= 1 && slot.index <= 27) {
                // --- ケース2: インベントリメイン ---
                float targetX = GUI_LAYOUT.getPointX(inventoryX);
                float targetY = GUI_LAYOUT.getPointY(inventoryY);
                int i = slot.index - 1;
                finalX = targetX + (-80 + (18 * (i % 9)));
                finalY = targetY + (-37 + (18 * (int) (i / 9)));
                
            } else if (slot.index >= 28 && slot.index <= 36) {
                // --- ケース3: ホットバー ---
                float targetX = GUI_LAYOUT.getPointX(inventoryX);
                float targetY = GUI_LAYOUT.getPointY(inventoryY);
                int i = slot.index - 1;
                finalX = targetX + (-80 + (18 * (i % 9)));
                finalY = targetY + (-37 + 18 * 3 + 4);
                
            } else {
                // その他（デフォルト）
                super.renderSlotHighlight(guiGraphics, slot, mouseX, mouseY, partialTick);
                return;
            }
            
            float size = 16.0f * currentScale;
            
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, 0, 100); // アイテムより手前に描画
            
            if (slot.index == 0) {
                float hs = size / 2.0f;
                guiGraphics.fill(RenderType.guiOverlay(), (int)(finalX - hs), (int)(finalY - hs), (int)(finalX + hs), (int)(finalY + hs), 0x80FFFFFF);
            } else {
                guiGraphics.fill(RenderType.guiOverlay(), (int)finalX, (int)finalY, (int)(finalX + size), (int)(finalY + size), 0x80FFFFFF);
            }
            
            guiGraphics.pose().popPose();
        }
    }
    
    public void blitWithGradient(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int color) {
        Matrix4f matrix = guiGraphics.pose().last().pose();
        VertexConsumer buffer = guiGraphics.bufferSource().getBuffer(RenderType.entityTranslucent(texture));
        
        float u0 = u / (float)textureWidth;
        float v0 = v / (float)textureHeight;
        float u1 = (u + (float)width) / (float)textureWidth;
        float v1 = (v + (float)height) / (float)textureHeight;
        
        // 頂点定義のヘルパー
        addVertex(buffer, matrix, (float)x, (float)y, u0, v0, color); // 左上(白)
        addVertex(buffer, matrix, (float)x, (float)(y + height), u0, v1, 0xFFFFFFFF);   // 左下(変色)
        addVertex(buffer, matrix, (float)(x + width), (float)(y + height), u1, v1, 0xFFFFFFFF); // 右下(変色)
        addVertex(buffer, matrix, (float)(x + width), (float)y, u1, v0, color); // 右上(白)
        
        // 即時反映させるためにflushを呼ぶ（任意ですが安全です）
        guiGraphics.flush();
    }
    
    private void addVertex(VertexConsumer buffer, Matrix4f matrix, float x, float y, float u, float v, int color) {
        buffer.addVertex(matrix, x, y, 0.0F)
                .setColor(color)
                .setUv(u, v)
                .setUv1(0, 10)  // これを追加
                .setUv2(240, 240)                 // フルブライトのライトマップ値
                .setNormal(0.0F, 0.0F, 1.0F);      // 正面を向く法線
    }
    
    private int calculateColor(float ratio, int alpha) {
        int r, g, b;
        float randomOffset = (float)(Math.random() * 0.6); // 20%程度の個体差
        
        if (ratio < 0.5f) { // 低温〜中温 (赤〜オレンジ)
            r = 200 + (int)(55 * ratio);
            g = (int)(200 * (ratio + randomOffset));
            b = (int)(50 * ratio);
        } else { // 高温 (白〜青)
            r = (int)(255 * (1.0f - ratio + randomOffset));
            g = (int)(255 * (0.8f + randomOffset * 0.5f));
            b = 255;
        }
        
        // clampして0-255に収める
        r = Mth.clamp(r, 0, 255);
        g = Mth.clamp(g, 0, 255);
        b = Mth.clamp(b, 0, 255);
        
        return (alpha << 24) | (r << 16) | (g << 8) | b;
    }
    
    private class Particles {
        private boolean isFast = true;
        
        private long spawnWait;
        private final float startPointX, startPointY; // 出現相対位置
        private int x, y, z = 0; // 出現位置
        private float offsetX, offsetY; // 動いた分の距離
        private float vx, vy;           // 速度
        private int life;
        private long maxAge;
        private float colorRatio;
        private float particleScale;
        // TODO 色を範囲内でランダム化と温度に合わせて色の範囲を変える
        // TODO 温度が高くなると炎が若干大きく、低くなると逆に
        
        public Particles(float startX, float startY) {
            startPointX = startX;
            startPointY = startY;
            spawnWait = (long) (System.currentTimeMillis() + Math.random() * 1000);
        }
        
        private void init(float thermal) {
            long time = System.currentTimeMillis();
            vx = (float) ((Math.random() - 0.5) * 2.0f) / 100 * 0.1f; // 左右に少し揺れる
            vy = (float) ((Math.random() * -3.0) - 1.0f) / 100 * 0.05f; // 上に向かって飛ぶ
            life = 1000 + (int) ((Math.random() - 0.5) * 1000);
            maxAge = time + life;
            offsetX = (float)(Math.random() - 0.5) * 0.01f;
            offsetY = (float)(Math.random() - 0.5) * 0.001f;
            particleScale = (float) GUI_LAYOUT.getScale(1.5);
            
            // 色の計算
            
            float minTemp = 0f;
            float maxTemp = 1000f;
            // 0.0 ~ 1.0 の範囲に正規化
            colorRatio = Mth.clamp((thermal - minTemp) / (maxTemp - minTemp), 0.0f, 1.0f);
            
            vx *= (1 + colorRatio * 1.5f);
            vy *= (1 + colorRatio * 0.4f);
        }
        
        public void render(GuiGraphics guiGraphics, float thermal) {
            long time = System.currentTimeMillis();
            
            if (isFast && time > spawnWait) {
                init(thermal);
                isFast = false;
            }
            
            offsetX += vx;
            offsetY += vy;
            // 寿命が来たらリセットする（テスト用）
            if (time > maxAge) {
                init(thermal);
            }
            
            // 寿命に応じて透明度を変える計算 (1.0 -> 0.0)
            float lifeRatio = ((float) (maxAge - time) / life / 2);
            int alpha = (int) (lifeRatio * 255);
            
            int color = calculateColor(colorRatio, alpha);
            
            offsetX *= 0.9f;
            vy -= 0.000001f;
            
            x = GUI_LAYOUT.getPointX(startPointX + offsetX);
            y = GUI_LAYOUT.getPointY(startPointY + offsetY);
            
            
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x, y, z);
            guiGraphics.pose().scale(particleScale, particleScale, particleScale);
            if (vx > 0) {
                guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees((float) (maxAge - time) * 0.1f));
            } else {
                guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees((float) (maxAge - time) * -0.1f));
            }
            guiGraphics.fill(-5, -5, 5, 5, color);
            guiGraphics.pose().popPose();
        }
    }
    
}
