package io.github.mrbest2525.magicalmechanics.client.item.bewlr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

public class ModClientExtensions {
    private static MachineFrameItemRenderer MACHINE_FRAME_RENDERER;
    private static MFLinkStaffItemRenderer MF_LINK_STAFF_RENDERER;
    
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
    
    public static BlockEntityWithoutLevelRenderer getMFLinkStaffRenderer() {
        if (MF_LINK_STAFF_RENDERER == null) {
            Minecraft mc = Minecraft.getInstance();
            MF_LINK_STAFF_RENDERER = new MFLinkStaffItemRenderer(
                    mc.getBlockEntityRenderDispatcher(),
                    mc.getEntityModels()
            );
        }
        return MF_LINK_STAFF_RENDERER;
    }
}
