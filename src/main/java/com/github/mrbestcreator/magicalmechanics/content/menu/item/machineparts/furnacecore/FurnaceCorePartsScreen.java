package com.github.mrbestcreator.magicalmechanics.content.menu.item.machineparts.furnacecore;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import com.github.mrbestcreator.magicalmechanics.content.menu.util.GuiLayout;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FurnaceCorePartsScreen extends AbstractContainerScreen<FurnaceCorePartsMenu> {
    
    private final GuiLayout GUI_LAYOUT = new GuiLayout(this.width, this.height);
    
    private final ResourceLocation FIRE_STAND_TEXTURE = ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "textures/gui/item/frame_parts/core/furnace_core/fire_stand.png");
    
    private final float fireX = 0.5f;
    private final float fireY = 0.3f;
    private final List<Particles> particlesList = new ArrayList<>();
    
    public FurnaceCorePartsScreen(FurnaceCorePartsMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        for (int i = 0; i < 100; i++) {
            particlesList.add(new Particles(fireX, fireY));
        }
    }
    
    @Override
    protected void init() {
        super.init();
        GUI_LAYOUT.updateSize(this.width, this.height);
    }
    
    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderables.forEach(r -> r.render(guiGraphics, mouseX, mouseY, partialTick));
        this.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        
    }
    
    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float v, int i, int i1) {
        // TODO 四角いパーティクル的なので(■)燃えてる時に炎があがっていくような動的Screenにしたい
        float fireStandScale = (float) GUI_LAYOUT.getScale(5);
        
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(GUI_LAYOUT.getPointX(fireX), GUI_LAYOUT.getPointY(fireY), -100);
        guiGraphics.pose().scale(fireStandScale, fireStandScale, fireStandScale);
        guiGraphics.pose().translate(-32, -32, 0);
        guiGraphics.blit(FIRE_STAND_TEXTURE, 0, 0, 0, 0, 64, 64, 64, 64);
        guiGraphics.pose().popPose();
        
        ContainerData data = this.menu.data;
        
        if (data.get(0) == 1) {
            particlesList.forEach(particles -> particles.render(guiGraphics, Float.intBitsToFloat(data.get(1))));
            
        }
        guiGraphics.drawString(this.font, "温度: " + Float.intBitsToFloat(data.get(1)) + "℃", GUI_LAYOUT.getPointX(0.5), GUI_LAYOUT.getPointY(0.5), 0xFFFFFFFF, true);
        
    }
    
    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
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
            particleScale = (float) GUI_LAYOUT.getScale(2.5);
            
            // 色の計算
            
            float currentTemp = thermal; // 摂氏を取得
            float minTemp = 0f;
            float maxTemp = 1000f;
            // 0.0 ~ 1.0 の範囲に正規化
            colorRatio = Mth.clamp((currentTemp - minTemp) / (maxTemp - minTemp), 0.0f, 1.0f);
            
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
            guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees((float)(maxAge - time) * 0.1f));
            guiGraphics.fill(-5, -5, 5, 5, color);
            guiGraphics.pose().popPose();
        }
        
        private int calculateColor(float ratio, int alpha) {
            int r, g, b;
            float randomOffset = (float)(Math.random() * 0.2); // 20%程度の個体差
            
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
    }
    
}
