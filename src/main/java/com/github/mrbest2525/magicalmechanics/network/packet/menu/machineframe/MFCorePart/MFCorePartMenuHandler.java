package com.github.mrbest2525.magicalmechanics.network.packet.menu.machineframe.MFCorePart;

import com.github.mrbest2525.magicalmechanics.content.menu.item.machineparts.energycore.EnergyCorePartMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class MFCorePartMenuHandler {
    /**
     * サーバーから送られてきたエネルギー同期パケットを処理する
     */
    public static void handleEnergyCoreSync(final MFCorePartMenuSyncPayload payload, final IPayloadContext context) {
        // メインスレッドで実行することを保証
        context.enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if (player == null) return;
            
            AbstractContainerMenu currentMenu = player.containerMenu;
            
            // 現在開いているMenuがEnergyCorePartMenu（MFCorePartMenu）かチェック
            if (currentMenu instanceof EnergyCorePartMenu energyMenu) {
                // Menu側の値を更新
                energyMenu.updateFromPayload(payload);
            }
        });
    }
}
