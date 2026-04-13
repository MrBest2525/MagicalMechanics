package com.github.mrbest2525.magicalmechanics.network.packet.menu;

import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameSlot;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.FrameCore;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.FrameSide;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {
    public static void handleOpenPart(final OpenFramePartPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            Level level = player.level();
            BlockPos blockPos = payload.pos();
            if(level.getBlockEntity(blockPos) instanceof MachineFrameBlockEntity machineFrameBlockEntity) {
                // partId に応じて開くメニューを切り替える
                // 例: 0=SIDE, 1=CORE, 2=FRAME
                switch (payload.partId()) {
                    case 0:
                        if (machineFrameBlockEntity.getPart(MachineFrameSlot.SIDE).getItem() instanceof FrameSide frameSide) {
                            var instance = frameSide.createInstance(machineFrameBlockEntity);
                            var menu = instance.getMenu(0, player.getInventory(), machineFrameBlockEntity);
                            
                            if (menu != null) {
                                player.openMenu(new SimpleMenuProvider(
                                        (id, inv, p) -> instance.getMenu(id, inv, machineFrameBlockEntity), // 変数instanceをそのまま使う
                                        Component.literal("SIDE")
                                ), blockPos);
                            }
                        }
                        break;
                    case 1:
                        if (machineFrameBlockEntity.getPart(MachineFrameSlot.CORE).getItem() instanceof FrameCore frameCore) {
                            var instance = frameCore.createInstance(machineFrameBlockEntity);
                            var menu = instance.getMenu(0, player.getInventory(), machineFrameBlockEntity);
                            
                            if (menu != null) {
                                player.openMenu(new SimpleMenuProvider(
                                        (id, inv, p) -> instance.getMenu(id, inv, machineFrameBlockEntity), // 変数instanceをそのまま使う
                                        Component.literal("CORE")
                                ), blockPos);
                            }
                        }
                        break;
                    case 2:
                        player.openMenu(new SimpleMenuProvider(
                                (id, inv, p) -> machineFrameBlockEntity.getMenu(id, inv, machineFrameBlockEntity),
                                Component.literal("Frame")
                        ), blockPos);
                        break;
                }
            }
        });
    }
}
