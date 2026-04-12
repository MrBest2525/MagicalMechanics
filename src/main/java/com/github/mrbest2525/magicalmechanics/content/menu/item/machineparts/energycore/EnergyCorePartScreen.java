package com.github.mrbest2525.magicalmechanics.content.menu.item.machineparts.energycore;

import com.github.mrbest2525.magicalmechanics.api.SourceType;
import com.github.mrbest2525.magicalmechanics.content.menu.util.GuiLayout;
import com.github.mrbest2525.magicalmechanics.content.menu.util.MMAbstractContainerScreen;
import com.github.mrbest2525.magicalmechanics.network.packet.menu.machineframe.MFCorePart.MFCorePartMenuSyncPayload;
import com.github.mrbest2525.magicalmechanics.util.MMTextures;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

public class EnergyCorePartScreen extends MMAbstractContainerScreen<EnergyCorePartMenu> {
    
    private final GuiLayout GUI_LAYOUT;
    
    private final float energyBarX = 0.5f;
    private final float energyBarY = 0.2f;
    
    // --- カオスバー制御用 ---
    private float displayProgress = 0.5f;
    private float targetProgress = 0.5f;
    private float startProgress = 0.5f; // 補間の開始地点
    private long lastLerpTime = 0;
    private long currentLerpDuration = 1000;
    
    public EnergyCorePartScreen(EnergyCorePartMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        GUI_LAYOUT = new GuiLayout(this.width, this.height);
    }
    
    @Override
    protected void init() {
        super.init();
        GUI_LAYOUT.updateSize(this.width, this.height);
    }
    
    @Override
    public void addRenderSlot(@NotNull GuiGraphics gui, @NotNull Slot slot) {
    
    }
    
    @Override
    protected void addRenderSlotHighlight(@NotNull GuiGraphics gui, @NotNull Slot slot, int mouseX, int mouseY, float partialTick) {
    
    }
    
    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        
        MFCorePartMenuSyncPayload data = menu.getSyncData();
        
        // --- 1. テキスト表示 (中央揃え) ---
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(GUI_LAYOUT.getPointX(0.5), GUI_LAYOUT.getPointY(0.25), 0);
        
        String displayStr = (data == null) ? "Connecting... / ... MF" : getSmartDisplayString(data);
        int textWidth = this.font.width(displayStr);
        guiGraphics.drawString(this.font, displayStr, -textWidth / 2, 0, 0xFFFFFF, true);
        guiGraphics.pose().popPose();
        
        // --- 2. 縦方向の充電バーの描画 ---
        renderEnergyBar(guiGraphics, data);
    }
    
    /**
     * EnergyBarの表示
     */
    private void renderEnergyBar(GuiGraphics guiGraphics, MFCorePartMenuSyncPayload data) {
        float progress = (data == null) ? 0.0f : getEnergyProgress(data);
        long now = System.currentTimeMillis();
        
        if (data != null && data.mode() == 2) { // Unlimited
            
            // 1. 目標設定ロジック
            if (now - lastLerpTime > currentLerpDuration) {
                startProgress = displayProgress; // 現在の地点をスタートにする
                
                targetProgress = (float) Math.random();
                lastLerpTime = now;
            }
            
            // 2. 滑らかな補間 (イージング)
            float delta = (float) (now - lastLerpTime) / currentLerpDuration;
            delta = Math.min(1.0f, delta);
            
            // クルッと回るような Sin補間 (0-1)
            float smoothDelta = (float) (1.0 - Math.cos(delta * Math.PI)) / 2.0f;
            
            // startからtargetへ smoothDelta分だけ移動
            displayProgress = startProgress + (targetProgress - startProgress) * smoothDelta;
            progress = displayProgress;
            
        }
        
        MMTextures.TextureAsset bar = MMTextures.GUI.UTIL_BAR_MEMORY;
        
        guiGraphics.pose().pushPose();
        
        guiGraphics.pose().translate(GUI_LAYOUT.getPointX(energyBarX), GUI_LAYOUT.getPointY(energyBarY), 0);
        
        MMTextures.blitAssetCentered(guiGraphics, MMTextures.GUI.UTIL_BAR_FRAME, 0, 0);
        
        if (progress > 0) {
            // バーの左端を計算（中心から幅の半分だけ左に戻る）
            int startX = -(bar.width() / 2);
            int startY = -(bar.height() / 2);
            int fillWidth = (int) (bar.width() * progress);
            
            // 2. SourceTypeから色を取得してメインのバーを描画
            int color = SourceType.MagicalFlux.getColor();
            float r = ((color >> 16) & 0xFF) / 255f;
            float g = ((color >> 8) & 0xFF) / 255f;
            float b = (color & 0xFF) / 255f;
            
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(r, g, b, 1.0f);
            
            // 左から右へ伸びる（fillWidth分だけ切り出して描画）
            guiGraphics.blit(bar.location(), startX, startY, bar.u(), bar.v(), fillWidth, bar.height(), bar.texWidth(), bar.texHeight());
            
            // 3. エンチャントオーラ（加算合成で脈動させる）
            float pulse = (float) Math.sin(System.currentTimeMillis() / 400.0) * 0.15f + 0.4f;
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            RenderSystem.setShaderColor(r, g, b, pulse);
            
            guiGraphics.blit(bar.location(), startX, startY, bar.u(), bar.v(), fillWidth, bar.height(), bar.texWidth(), bar.texHeight());
            
            // 後片付け
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            RenderSystem.disableBlend();
        }
        
        guiGraphics.pose().popPose();
    }
    
    /**
     * エネルギーの割合(0.0 - 1.0)を計算する
     */
    private float getEnergyProgress(MFCorePartMenuSyncPayload data) {
        if (data == null) return 0.0f;
        if (data.mode() == 2) return 1.0f; // 無制限モード
        
        if (data.mode() == 0) { // 正確モード (long)
            if (data.maxRaw() <= 0) return 0f;
            return (float) ((double) data.currentRaw() / data.maxRaw());
        } else { // 指数モード (10^n)
            if (data.maxMantissa() <= 0) return 0f;
            
            // 指数の差を利用して比率を出す: (cMant / mMant) * 10^(cExp - mExp)
            int expDiff = data.currentExponent() - data.maxExponent();
            double ratio = ((double) data.currentMantissa() / data.maxMantissa()) * Math.pow(10, expDiff);
            
            return (float) Math.min(1.0, Math.max(0.0, ratio));
        }
    }
    
    /**
     * 接頭辞(K, M, G, T...)と指数表記をスマートに切り替える
     */
    private String getSmartDisplayString(MFCorePartMenuSyncPayload p) {
        // 1. 数値が 0 の時のガード（0.000 E+0 などを防ぐ）
        if (p.mode() != 0 && p.currentMantissa() == 0 && p.currentRaw() == 0) {
            return "0 / " + (p.mode() == 2 ? "∞" : "...") + " MF";
        }
        
        // 2. モード別の現在値文字列生成
        String currentStr;
        if (p.mode() == 0) {
            currentStr = formatWithUnit(p.currentRaw());
        } else if (p.mode() == 2 && p.currentRaw() != 0) {
            // 無制限モードで long 範囲内の場合
            currentStr = formatWithUnit(p.currentRaw());
        } else {
            // 指数・巨大数モード
            currentStr = formatPowerSmart(p.currentMantissa(), p.currentExponent());
        }
        
        // 3. 最大値文字列生成
        String maxStr = switch (p.mode()) {
            case 0 -> formatWithUnit(p.maxRaw());
            case 2 -> "∞";
            default -> "...";
        };
        
        return currentStr + " / " + maxStr + " MF";
    }
    
    private String formatPowerSmart(int mantissa, int exponent) {
        if (mantissa == 0) return "0";
        
        String[] units = {"", "K", "M", "G", "T", "P", "E"};
        int unitIndex = exponent / 3;
        
        if (unitIndex < units.length) {
            double base = mantissa / 1000.0;
            // 10の(exponent % 3)乗を掛けて小数点をスライド
            double shifted = base * Math.pow(10, exponent % 3);
            return String.format("%.3f %s", shifted, units[unitIndex]);
        }
        
        // エクサ(10^18)を超える場合
        return String.format("%d.%03d E+%d", mantissa / 1000, mantissa % 1000, exponent);
    }
    
    /**
     * long値を適切な単位（K, M, G, T...）に変換
     */
    private String formatWithUnit(long value) {
        if (value < 1000) return String.valueOf(value);
        
        String[] units = {"", "K", "M", "G", "T", "P", "E"};
        double dValue = (double) value;
        int unitIndex = 0;
        
        // 1000で割っていき、単位を特定する（浮動小数点の誤差を回避）
        while (dValue >= 1000 && unitIndex < units.length - 1) {
            dValue /= 1000.0;
            unitIndex++;
        }
        
        return String.format("%.3f %s", dValue, units[unitIndex]);
    }
}
