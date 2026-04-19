package io.github.mrbest2525.magicalmechanics.content.item.wirelesslinker.mfwirelesslinker;

import io.github.mrbest2525.magicalmechanics.api.SourceType;
import io.github.mrbest2525.magicalmechanics.content.block.mf.wireless.IWirelessLinkableSource;
import io.github.mrbest2525.magicalmechanics.content.block.mf.wireless.IWirelessLinkableTarget;
import io.github.mrbest2525.magicalmechanics.content.item.ModItemDataComponents;
import io.github.mrbest2525.magicalmechanics.content.item.wirelesslinker.WirelessLinkerMode;
import io.github.mrbest2525.magicalmechanics.util.MMLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
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
        WirelessLinkerMode mode = stack.getOrDefault(ModItemDataComponents.WIRELESS_LINK_MODE, WirelessLinkerMode.SOURCE);
        
        // スニーク時のみリンク操作を有効化
        if (!player.isCrouching()) return InteractionResult.PASS;
        
        // --- 2. ソース（接続元：Adapterなど）を叩いた場合 ---
        if (mode.equals(WirelessLinkerMode.SOURCE) && be instanceof IWirelessLinkableSource linkableSource) {
            if (linkableSource.canAcceptLinkSource(TYPE, player)) {
                BlockPos sourcePos = linkableSource.getSourcePos(TYPE);
                if (sourcePos != null) {
                    // スタックにソースの座標を記録する
                    stack.set(ModItemDataComponents.LINK_BLOCK_POS, sourcePos);
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
        if (mode.equals(WirelessLinkerMode.TARGET) && be instanceof IWirelessLinkableTarget linkableTarget) {
            BlockPos savedSourcePos = stack.get(ModItemDataComponents.LINK_BLOCK_POS);
            
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
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        
        // スニーク（シフト）中のみ切り替えを実行
        if (player.isCrouching()) {
            // 現在のモードを取得（未設定ならデフォルトで SOURCE）
            WirelessLinkerMode currentMode = stack.getOrDefault(ModItemDataComponents.WIRELESS_LINK_MODE, WirelessLinkerMode.SOURCE);
            
            // モードを反転させる
            WirelessLinkerMode nextMode = (currentMode == WirelessLinkerMode.SOURCE) ? WirelessLinkerMode.TARGET : WirelessLinkerMode.SOURCE;
            
            // コンポーネントにセット（これでサーバー・クライアント両方に同期されます）
            stack.set(ModItemDataComponents.WIRELESS_LINK_MODE, nextMode);
            
            if (level.isClientSide) {
                // 切り替え時の演出（カチャッという音とアクションバー表示）
                player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.5f, 1.5f);
                
                // モード名を翻訳して表示（MMLang などにキーを登録しておくと良いです）
                String modeKey = nextMode == WirelessLinkerMode.SOURCE ? MMLang.Msg.Actionbar.Linker.SET_SOURCE_MODE : MMLang.Msg.Actionbar.Linker.SET_TARGET_MODE;
                player.displayClientMessage(Component.translatable(modeKey).withStyle(style -> style.withColor(getColor(stack))), true);
            }
            
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        
        return InteractionResultHolder.pass(stack);
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        BlockPos pos = stack.get(ModItemDataComponents.LINK_BLOCK_POS);
        
        String mode = stack.getOrDefault(ModItemDataComponents.WIRELESS_LINK_MODE, WirelessLinkerMode.SOURCE).equals(WirelessLinkerMode.SOURCE) ? MMLang.Tooltip.Item.MagicalMechanics.MFLinkStaff.SOURCE_MODE : MMLang.Tooltip.Item.MagicalMechanics.MFLinkStaff.TARGET_MODE;
        
        tooltip.add(Component.translatable(MMLang.Tooltip.Item.MagicalMechanics.MFLinkStaff.MODE, Component.translatable(mode)).withStyle(style -> style.withColor(getColor(stack))));
        
        if (pos != null) {
            tooltip.add(Component.translatable(MMLang.Tooltip.Item.MagicalMechanics.MFLinkStaff.LINK, Component.translatable(MMLang.Tooltip.Item.MagicalMechanics.MFLinkStaff.BLOCK_POS, pos.getX(), pos.getY(), pos.getZ())).withStyle(ChatFormatting.GOLD));
        }
    }
    
    public int getColor(ItemStack stack) {
        WirelessLinkerMode mode = stack.getOrDefault(ModItemDataComponents.WIRELESS_LINK_MODE, WirelessLinkerMode.SOURCE);
        return mode == WirelessLinkerMode.SOURCE ? 0xFF00FFFF : 0xFFFF00FF; // 例: 青と紫
    }
}
