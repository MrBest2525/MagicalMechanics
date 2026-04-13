package com.github.mrbest2525.magicalmechanics.network.packet.menu.machineframe.MFCorePart;

import net.neoforged.neoforge.network.handling.IPayloadContext;

public class MFCorePartMenuHandler {
    /**
     * サーバーから送られてきたエネルギー同期パケットを処理する
     */
    public static void handleEnergyCoreSync(final MFCorePartMenuSyncPayload payload, final IPayloadContext context) {
        // メインスレッドで実行することを保証
        context.enqueueWork(() -> {
            MFCorePartMenuClientHandler.processSync(payload);
        });
    }
}
