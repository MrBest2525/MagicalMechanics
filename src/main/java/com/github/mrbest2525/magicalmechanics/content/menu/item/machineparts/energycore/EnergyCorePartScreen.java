package com.github.mrbest2525.magicalmechanics.content.menu.item.machineparts.energycore;

import com.github.mrbest2525.magicalmechanics.MagicalMechanics;
import com.github.mrbest2525.magicalmechanics.content.menu.util.GuiLayout;
import com.github.mrbest2525.magicalmechanics.content.menu.util.MMAbstractContainerScreen;
import com.github.mrbest2525.magicalmechanics.network.packet.menu.machineframe.MFCorePart.MFCorePartMenuSyncPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

public class EnergyCorePartScreen extends MMAbstractContainerScreen<EnergyCorePartMenu> {
    
    private final GuiLayout GUI_LAYOUT;
    
    private final ResourceLocation MOD_GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "textures/gui/inventory_slot/carpet4_1_64x64.png");
    
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
        if (data == null) return;
        
        // --- 1. テキスト表示 (中央揃え) ---
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(GUI_LAYOUT.getPointX(0.5), GUI_LAYOUT.getPointY(0.25), 0);
        String displayStr = getSmartDisplayString(data);
        int textWidth = this.font.width(displayStr);
        guiGraphics.drawString(this.font, displayStr, -textWidth / 2, 0, 0xFFFFFF, true);
        guiGraphics.pose().popPose();
        
        // --- 2. 縦方向の充電バーの描画 ---
        renderEnergyBar(guiGraphics, data);
    }
    
    /**
     * エネルギーの割合(0.0 - 1.0)を計算する
     */
    private float getEnergyProgress(MFCorePartMenuSyncPayload data) {
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
     * 実際にバーを描画する処理
     */
    private void renderEnergyBar(GuiGraphics gui, MFCorePartMenuSyncPayload data) {
        float progress = getEnergyProgress(data);
        
        // バーの位置とサイズ (テクスチャに合わせて調整)
        int barX = GUI_LAYOUT.getPointX(0.5);
        int barY = GUI_LAYOUT.getPointY(0.45);
        int width = 12;
        int height = 50;
        
        int fillHeight = (int) (height * progress);
        int emptyHeight = height - fillHeight;
        
        if (fillHeight > 0) {
            // 下から上にせり上がる描画
            // y座標を emptyHeight 分下げ、テクスチャのV座標も同じだけ下げる
            gui.blit(
                    MOD_GUI_TEXTURE,
                    barX, barY + emptyHeight,        // 画面上の描画開始位置(Y)
                    176, 0 + emptyHeight,            // テクスチャ上の「満タンバー」のV座標
                    width, fillHeight                // 描画する高さ
            );
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
