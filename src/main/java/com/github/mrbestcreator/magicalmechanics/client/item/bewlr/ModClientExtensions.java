package com.github.mrbestcreator.magicalmechanics.client.item.bewlr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

public class ModClientExtensions {
    private static MachineFrameItemRenderer MACHINE_FRAME_RENDERER;
    
    public static BlockEntityWithoutLevelRenderer getMachineFrameItemRenderer() {
        if (MACHINE_FRAME_RENDERER == null) {
            Minecraft mc = Minecraft.getInstance();
            MACHINE_FRAME_RENDERER = new MachineFrameItemRenderer(
                    mc.getBlockEntityRenderDispatcher(),
                    mc.getEntityModels()
            );
        }
        return MACHINE_FRAME_RENDERER;
    }
}
