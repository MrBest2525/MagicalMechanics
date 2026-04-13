package com.github.mrbest2525.magicalmechanics.network.packet.menu.machineframe.MFCorePart;

import com.github.mrbest2525.magicalmechanics.content.menu.item.machineparts.energycore.EnergyCorePartMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class MFCorePartMenuClientHandler {
    // このクラスはクライアント環境でしかロードされないように運用する
    public static void processSync(MFCorePartMenuSyncPayload payload) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        
        AbstractContainerMenu currentMenu = player.containerMenu;
        if (currentMenu instanceof EnergyCorePartMenu energyMenu) {
            energyMenu.updateFromPayload(payload);
        }
    }
}
