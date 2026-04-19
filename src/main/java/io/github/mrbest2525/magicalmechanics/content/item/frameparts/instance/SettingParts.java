package io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance;

import io.github.mrbest2525.magicalmechanics.content.menu.block.machine.frame.FrameBlockSettingPartsScreen;
import net.minecraft.client.gui.GuiGraphics;

public interface SettingParts {
    SettingPartsInstance createInstance();
    
    /**
     * 左パネルの静的情報を描画し、描画したトータルの高さを返す
     * @return 描画したコンテンツの総ピクセル高さ
     */
    int renderStaticInfo(
            FrameBlockSettingPartsScreen screen,
            GuiGraphics guiGraphics,
            int width,
            int height,
            double relMouseX,
            double relMouseY,
            float partialTick
    );
}
