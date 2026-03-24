package com.github.mrbest2525.magicalmechanics.network.packet;

import com.github.mrbest2525.magicalmechanics.MagicalMechanics;
import com.github.mrbest2525.magicalmechanics.network.packet.menu.OpenFramePartPayload;
import com.github.mrbest2525.magicalmechanics.network.packet.menu.ServerPayloadHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = MagicalMechanics.MODID)
public class ModPackets {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(MagicalMechanics.MODID).versioned("1.0.0");
        
        registrar.playToServer(
                OpenFramePartPayload.TYPE,
                OpenFramePartPayload.CODEC,
                ServerPayloadHandler::handleOpenPart
        );
    }
}
