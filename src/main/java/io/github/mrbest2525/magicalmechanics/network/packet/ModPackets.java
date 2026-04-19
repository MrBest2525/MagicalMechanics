package io.github.mrbest2525.magicalmechanics.network.packet;

import io.github.mrbest2525.magicalmechanics.MagicalMechanics;
import io.github.mrbest2525.magicalmechanics.network.packet.menu.OpenFramePartPayload;
import io.github.mrbest2525.magicalmechanics.network.packet.menu.ServerPayloadHandler;
import io.github.mrbest2525.magicalmechanics.network.packet.menu.machineframe.MFCorePart.MFCorePartMenuHandler;
import io.github.mrbest2525.magicalmechanics.network.packet.menu.machineframe.MFCorePart.MFCorePartMenuSyncPayload;
import io.github.mrbest2525.magicalmechanics.network.packet.menu.machineframe.settingpert.DeselectPartPayload;
import io.github.mrbest2525.magicalmechanics.network.packet.menu.machineframe.settingpert.MachineFrameSettingPartPayloadHandler;
import io.github.mrbest2525.magicalmechanics.network.packet.menu.machineframe.settingpert.SelectPartPayload;
import io.github.mrbest2525.magicalmechanics.network.packet.menu.machineframe.settingpert.SyncFramePartsPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = MagicalMechanics.MODID)
public class ModPackets {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(MagicalMechanics.MODID).versioned("0.3.0");
        
        registrar.playToServer(
                OpenFramePartPayload.TYPE,
                OpenFramePartPayload.CODEC,
                ServerPayloadHandler::handleOpenPart
        );
        
        registrar.playToClient(
                SyncFramePartsPayload.TYPE,
                SyncFramePartsPayload.CODEC,
                MachineFrameSettingPartPayloadHandler::handleSync
        );
        
        registrar.playBidirectional(
                DeselectPartPayload.TYPE,
                DeselectPartPayload.CODEC,
                MachineFrameSettingPartPayloadHandler::handleDeselect
        );
        
        registrar.playBidirectional(
                SelectPartPayload.TYPE,
                SelectPartPayload.CODEC,
                MachineFrameSettingPartPayloadHandler::handleSelect
        );
        
        registrar.playToClient(
                MFCorePartMenuSyncPayload.TYPE,
                MFCorePartMenuSyncPayload.CODEC,
                MFCorePartMenuHandler::handleEnergyCoreSync
        );
    }
}
