package com.github.mrbestcreator.magicalmechanics.network.packet.menu;

import com.github.mrbestcreator.magicalmechanics.content.block.machine.frame.FrameBlockEntity;
import com.github.mrbestcreator.magicalmechanics.content.block.machine.frame.FrameSlot;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.FrameCore;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.FrameParts;
import com.github.mrbestcreator.magicalmechanics.content.menu.block.machine.frame.FrameBlockMenu;
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
            if(level.getBlockEntity(blockPos) instanceof FrameBlockEntity frameBlockEntity) {
                // partId に応じて開くメニューを切り替える
                // 例: 0=SIDE, 1=CORE, 2=FRAME
                switch (payload.partId()) {
                    case 0:
                        if (frameBlockEntity.getPart(FrameSlot.SIDE).getItem() instanceof FrameParts frameParts) {
                            var instance = frameParts.createInstance();
                            var menu = instance.getMenu(0, player.getInventory(), frameBlockEntity);
                            
                            if (menu != null) {
                                player.openMenu(new SimpleMenuProvider(
                                        (id, inv, p) -> instance.getMenu(id, inv, frameBlockEntity), // 変数instanceをそのまま使う
                                        Component.literal("SIDE")
                                ), blockPos);
                            }
                        }
                        break;
                    case 1:
                        if (frameBlockEntity.getPart(FrameSlot.CORE).getItem() instanceof FrameCore frameCore) {
                            var instance = frameCore.createInstance();
                            var menu = instance.getMenu(0, player.getInventory(), frameBlockEntity);
                            
                            if (menu != null) {
                                player.openMenu(new SimpleMenuProvider(
                                        (id, inv, p) -> instance.getMenu(id, inv, frameBlockEntity), // 変数instanceをそのまま使う
                                        Component.literal("CORE")
                                ), blockPos);
                            }
                        }
                        break;
                    case 2:
                        player.openMenu(new SimpleMenuProvider(
                                (id, inv, p) -> new FrameBlockMenu(id, inv, frameBlockEntity),
                                Component.literal("Frame")
                        ), blockPos);
                        break;
                }
            }
        });
    }
}
