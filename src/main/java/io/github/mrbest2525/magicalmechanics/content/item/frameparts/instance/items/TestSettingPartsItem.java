package io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.items;

import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.SettingParts;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.SettingPartsInstance;
import io.github.mrbest2525.magicalmechanics.content.menu.block.machine.frame.FrameBlockSettingPartsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

public class TestSettingPartsItem extends Item implements SettingParts {
    public TestSettingPartsItem(Item.Properties properties) {
        super(properties);
    }
    
    @Override
    public SettingPartsInstance createInstance() {
        return null;
    }
    
    @Override
    public int renderStaticInfo(FrameBlockSettingPartsScreen screen, GuiGraphics guiGraphics, int width, int height, double relMouseX, double relMouseY, float partialTick) {
        int currY = 0;
        Font font = Minecraft.getInstance().font;
        
        // タイトル
        guiGraphics.drawString(font, "§e§lパーツ解説", 0, currY, 0xFFFFFFFF);
        currY += 12;
        
        // 本文
        String text = "ここに非常に長い説明文が入ります...\n.\n..\n...\n....\n.....\n......\n.......\n........\n.........\n..........\n...........\n............\n.............\n..............\n...............\n................\n.................\n..................\n....................";
        var lines = font.split(Component.literal(text), width);
        for (var line : lines) {
            guiGraphics.drawString(font, line, 0, currY, 0xFFFFFFFF, false);
            currY += 10;
        }
        
        // 最後に描画した合計の高さを報告
        return currY + 5; // 下部に少し余白
    }
}
