package io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.items;

import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.SettingPart;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.SettingPartInstance;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.settingpart.MFWirelessChargerSettingPartInstance;
import io.github.mrbest2525.magicalmechanics.content.menu.block.machine.frame.FrameBlockSettingPartsScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;

public class MFWirelessChargerSettingPartItem extends Item implements SettingPart {
    public MFWirelessChargerSettingPartItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public SettingPartInstance createInstance() {
        return new MFWirelessChargerSettingPartInstance();
    }
    
    @Override
    public int renderStaticInfo(FrameBlockSettingPartsScreen screen, GuiGraphics guiGraphics, int width, int height, double relMouseX, double relMouseY, float partialTick) {
        return 0;
    }
}
