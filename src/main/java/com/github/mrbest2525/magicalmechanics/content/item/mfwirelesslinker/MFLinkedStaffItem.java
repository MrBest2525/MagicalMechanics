package com.github.mrbest2525.magicalmechanics.content.item.mfwirelesslinker;

import com.github.mrbest2525.magicalmechanics.api.SourceType;
import com.github.mrbest2525.magicalmechanics.content.block.mf.wireless.IWirelessLinkableSource;
import com.github.mrbest2525.magicalmechanics.content.block.mf.wireless.IWirelessLinkableTarget;
import com.github.mrbest2525.magicalmechanics.content.item.ModItemDataComponents;
import com.github.mrbest2525.magicalmechanics.util.MMLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MFLinkedStaffItem extends Item {
    
    private final SourceType TYPE = SourceType.MagicalFlux;
    
    public MFLinkedStaffItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if (level.isClientSide || player == null) return InteractionResult.SUCCESS;
        ItemStack stack = context.getItemInHand();
        BlockPos clickedPos = context.getClickedPos();
        BlockEntity be = level.getBlockEntity(clickedPos);
        
        // スニーク時のみリンク操作を有効化
        if (!player.isCrouching()) return InteractionResult.PASS;
        
        
        // --- 2. ソース（接続元：Adapterなど）を叩いた場合 ---
        if (be instanceof IWirelessLinkableSource linkableSource) {
            if (linkableSource.canAcceptLinkSource(TYPE, player)) {
                BlockPos sourcePos = linkableSource.getSourcePos(TYPE);
                if (sourcePos != null) {
                    // スタックにソースの座標を記録する
                    stack.set(ModItemDataComponents.LINKED_BLOCK_POS, sourcePos);
                    // 記録成功メッセージ（引数構成は維持）
                    player.displayClientMessage(Component.translatable(MMLang.Msg.Actionbar.Linker.SET_BLOCK_POS,
                            Component.translatable(MMLang.Msg.Actionbar.Linker.BLOCK_POS, sourcePos.getX(), sourcePos.getY(), sourcePos.getZ())), true);
                    return InteractionResult.CONSUME;
                }
            } else {
                player.displayClientMessage(Component.translatable(MMLang.Msg.Actionbar.Linker.ACCESS_FAILED), true);
                return InteractionResult.FAIL;
            }
        }
        
        // --- 1. ターゲット（接続先：MachineFrameなど）を叩いた場合 ---
        if (be instanceof IWirelessLinkableTarget linkableTarget) {
            BlockPos savedSourcePos = stack.get(ModItemDataComponents.LINKED_BLOCK_POS);
            
            // スタックに「ソースの座標」が保存されていれば、それをターゲットに書き込む
            if (savedSourcePos != null) {
                if (linkableTarget.canAcceptLinkTarget(TYPE, savedSourcePos, player)) {
                    linkableTarget.setTargetLink(TYPE, savedSourcePos);
                    // 成功メッセージ（引数構成は維持）
                    player.displayClientMessage(Component.translatable(MMLang.Msg.Actionbar.Linker.LINK_SUCCESSFUL,
                            Component.translatable(MMLang.Msg.Actionbar.Linker.BLOCK_POS, savedSourcePos.getX(), savedSourcePos.getY(), savedSourcePos.getZ())), true);
                    return InteractionResult.CONSUME;
                } else {
                    player.displayClientMessage(Component.translatable(MMLang.Msg.Actionbar.Linker.ACCESS_FAILED), true);
                    return InteractionResult.FAIL;
                }
            }
        }
        
        return InteractionResult.PASS;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        BlockPos pos = stack.get(ModItemDataComponents.LINKED_BLOCK_POS);
        
        if (pos != null) {
            tooltip.add(Component.translatable(MMLang.Tooltip.Item.MagicalMechanics.MFLinkStaff.LINKED, Component.translatable(MMLang.Tooltip.Item.MagicalMechanics.MFLinkStaff.BLOCK_POS, pos.getX(), pos.getY(), pos.getY())).withStyle(ChatFormatting.GOLD));
        }
    }
    
    public int getColor() {
        return 0xFF00FFFF;
    }
}
