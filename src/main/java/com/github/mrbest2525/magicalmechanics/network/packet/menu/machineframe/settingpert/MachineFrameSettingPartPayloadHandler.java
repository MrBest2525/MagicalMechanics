package com.github.mrbest2525.magicalmechanics.network.packet.menu.machineframe.settingpert;

import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import com.github.mrbest2525.magicalmechanics.content.menu.block.machine.frame.FrameBlockSettingPartsMenu;
import com.github.mrbest2525.magicalmechanics.content.menu.block.machine.frame.FrameBlockSettingPartsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class MachineFrameSettingPartPayloadHandler {
    /** Screen側で選ばれたUUIDを同期する */
    public static void handleDeselect(final DeselectPartPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            // スロット36 -> Manager.addPart() のロジック
            Player player = context.player();
            if (player.level().getBlockEntity(payload.pos()) instanceof MachineFrameBlockEntity be) {
                // 現在開いているMenuが正しいかチェック
                if (player.containerMenu instanceof FrameBlockSettingPartsMenu menu) {
                    menu.setSelectedPart(null);
                }
            }
        });
    }
    
    /** Screen側のUUID未選択を同期する */
    public static void handleSelect(final SelectPartPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            // Manager.removePart(uuid) -> スロット36 のロジック
            Player player = context.player();
            if (player.level().getBlockEntity(payload.pos()) instanceof MachineFrameBlockEntity be) {
                if (player.containerMenu instanceof FrameBlockSettingPartsMenu menu) {
                    menu.setSelectedPart(payload.uuid());
                }
            }
        });
    }
    
    /** クライアント側の表示を更新する (S2Cパケット用) */
    public static void handleSync(final SyncFramePartsPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            // Screenのリストを更新するロジック
            // Client側でのみ実行される処理
            if (context.player().level().isClientSide) {
                // クライアント専用の処理を呼び出す
                ClientHandler.updateScreen(payload);
            }
        });
    }
    
    // 物理クライアントでのみ読み込まれるように内部クラス（または別ファイル）に分ける
    private static class ClientHandler {
        private static void updateScreen(SyncFramePartsPayload payload) {
//            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
//            if (mc.screen instanceof FrameBlockSettingPartsScreen screen) {
//                // Screenに定義した更新メソッドを呼ぶ
//                screen.updatePartsList(payload.parts());
//            }
            Minecraft mc = Minecraft.getInstance();
            
            // 座標からBEを取得して、まずはデータを同期
            if (mc.level != null && mc.level.getBlockEntity(payload.pos()) instanceof MachineFrameBlockEntity be) {
                // BEのマネージャーを更新
                be.getSettingPartsManager().setAllPartsFromServer(payload.parts());
                
                // 現在開いているのがこのBEのScreenなら、UIを更新
                if (mc.screen instanceof FrameBlockSettingPartsScreen screen) {
                    // 明示的に型を指定して呼び出す
                    screen.updatePartsList(payload.parts());
                }
            }
        }
    }
    
    
    /** 共通：全プレイヤーにManagerの状態を同期する */
    private static void syncToAll(MachineFrameBlockEntity be, net.minecraft.core.BlockPos pos) {
        PacketDistributor.sendToAllPlayers(new SyncFramePartsPayload(pos, be.settingParts.getUUIDtoPartsItemStackMap()));
    }
}
