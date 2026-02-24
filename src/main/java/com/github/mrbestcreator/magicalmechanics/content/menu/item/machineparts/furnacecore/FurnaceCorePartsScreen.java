package com.github.mrbestcreator.magicalmechanics.content.menu.item.machineparts.furnacecore;

import com.github.mrbestcreator.magicalmechanics.content.menu.util.GuiLayout;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FurnaceCorePartsScreen extends AbstractContainerScreen<FurnaceCorePartsMenu> {
    
    private final GuiLayout GUI_LAYOUT = new GuiLayout(this.width, this.height);
    
    private final List<Particles> particlesList = new ArrayList<>();
    
    public FurnaceCorePartsScreen(FurnaceCorePartsMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        for (int i = 0; i < 100; i++) {
            particlesList.add(new Particles(0.5f, 0.5f));
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
        particlesList.forEach(particles -> particles.render(guiGraphics));
    }
    
    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }
    
    private class Particles {
        private final float startPointX, startPointY; // 出現相対位置
        private int x, y, z = 0; // 出現位置
        private float offsetX, offsetY; // 動いた分の距離
        private float vx, vy;           // 速度
        private int life;
        private long maxAge;
        private int color;
        // TODO 色を範囲内でランダム化と温度に合わせて色の範囲を変える
        // TODO 温度が高くなると炎が若干大きく、低くなると逆に
        
        public Particles(float startX, float startY) {
            startPointX = startX;
            startPointY = startY;
            color = 0xFF4400;
            init();
        }
        
        private void init() {
            long time = System.currentTimeMillis();
            vx = (float) ((Math.random() - 0.5) * 2.0f) / 100 * 0.1f; // 左右に少し揺れる
            vy = (float) ((Math.random() * -3.0) - 1.0f) / 100 * 0.05f; // 上に向かって飛ぶ
            life = 1000 + (int) ((Math.random() - 0.5) * 1000);
            maxAge = time + life;
            offsetX = (float)(Math.random() - 0.5) * 0.01f;
            offsetY = (float)(Math.random() - 0.5) * 0.001f;
        }
        
        public void render(GuiGraphics guiGraphics) {
            long time = System.currentTimeMillis();
            offsetX += vx;
            offsetY += vy;
            // 寿命が来たらリセットする（テスト用）
            if (time > maxAge) {
                init();
            }
            
            offsetX *= 0.9f;
            vy -= 0.000001f;
            
            x = GUI_LAYOUT.getPointX(startPointX + offsetX);
            y = GUI_LAYOUT.getPointY(startPointY + offsetY);
            
            // 寿命に応じて透明度を変える計算 (1.0 -> 0.0)
            float lifeRatio = ((float) (maxAge - time) / life / 2);
            int alpha = (int) (lifeRatio * 255);
            int color = (alpha << 24) | this.color;
            
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x, y, z);
            guiGraphics.pose().scale(0.5f, 0.5f, 0.5f);
            guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees((float)(maxAge - time) * 0.1f));
            guiGraphics.fill(-5, -5, 5, 5, color);
            guiGraphics.pose().popPose();
        }
    }
    
}
