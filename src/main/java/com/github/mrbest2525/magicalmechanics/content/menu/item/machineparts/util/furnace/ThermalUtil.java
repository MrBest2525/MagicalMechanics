package com.github.mrbest2525.magicalmechanics.content.menu.item.machineparts.util.furnace;

import net.minecraft.util.Mth;

public class ThermalUtil {
    public static int calculateColor(float ratio, int alpha) {
        int r, g, b;
        float randomOffset = (float)(Math.random() * 0.6); // 20%程度の個体差
        
        if (ratio < 0.75f) { // 低温〜中温 (赤〜オレンジ)
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
