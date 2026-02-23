package com.github.mrbestcreator.magicalmechanics;

import com.github.mrbestcreator.magicalmechanics.content.menu.ModMenus;
import com.github.mrbestcreator.magicalmechanics.content.menu.block.machine.frame.FrameBlockScreen;
import com.github.mrbestcreator.magicalmechanics.content.menu.item.machineparts.furnacecore.FurnaceCorePartsScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

//@EventBusSubscriber(modid = MagicalMechanics.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
@EventBusSubscriber(modid = MagicalMechanics.MODID, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.MACHINE_FRAME_MENU.get(), FrameBlockScreen::new);
        event.register(ModMenus.FURNACE_CORE_PARTS_MENU.get(), FurnaceCorePartsScreen::new);
    }
}
