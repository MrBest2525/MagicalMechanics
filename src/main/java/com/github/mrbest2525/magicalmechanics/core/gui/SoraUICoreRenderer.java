package com.github.mrbest2525.magicalmechanics.core.gui;

import net.minecraft.client.gui.GuiGraphics;

@FunctionalInterface
public interface SoraUICoreRenderer {
    /**
     * @param element     描画を実行している要素自身（IDやContextを保持）
     * @param guiGraphics Minecraftの描画システム
     * @param partialTick Tick間の補完
     * @param mouseX      現在のマウス位置（X）
     * @param mouseY      現在のマウス位置（Y）
     * @param window      Minecraft.getInstance().getWindow().getWindow()
     * @param isPressed   mouseClickedイベントが呼ばれたとき
     */
    void render(ISoraUICoreDrawingElement.ISoraUIDrawingMouseEventElement<?> element, GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, long window, boolean isPressed);
}
