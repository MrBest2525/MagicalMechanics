package com.github.mrbest2525.magicalmechanics.content.item.wrench;

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

public class WrenchItem extends Item {
    
    public WrenchItem(Properties properties) {
        super(properties);
    }
    
    public static WrenchMode getMode(ItemStack itemStack) {
        WrenchData wrenchData = itemStack.get(ModItemDataComponents.WRENCH_DATA);
        if (wrenchData == null) return WrenchMode.SIDE;
        else return wrenchData.mode();
    }
    
    public void toggleMode(ItemStack itemStack) {
        WrenchMode lastMode = getMode(itemStack);
        if (lastMode.equals(WrenchMode.SIDE)) itemStack.set(ModItemDataComponents.WRENCH_DATA, new WrenchData(WrenchMode.CORE));
        else itemStack.set(ModItemDataComponents.WRENCH_DATA, new WrenchData(WrenchMode.SIDE));
    }
    
    public void setMode(ItemStack itemStack, WrenchMode wrenchMode) {
        itemStack.set(ModItemDataComponents.WRENCH_DATA, new WrenchData(wrenchMode));
    }
    
    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) return InteractionResult.SUCCESS;
        
        // モード切替
        if (context.getPlayer() != null && context.getPlayer().isCrouching()) {
            toggleMode(context.getItemInHand());
            context.getPlayer().displayClientMessage(Component.translatable(MMLang.Msg.Actionbar.Wrench.MODE_CHANGE, getMode(context.getItemInHand()).toString()), true);
            return InteractionResult.CONSUME;
        }
        
        BlockPos pos = context.getClickedPos();
        BlockEntity be = level.getBlockEntity(pos);
        
        if (be instanceof WrenchInteractable wi) {
            return wi.onWrenchUse(context);
        }
        
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.SUCCESS;
        player.getCooldowns().addCooldown(context.getItemInHand().getItem(), 5);
        
        return InteractionResult.SUCCESS;
    }
    
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        WrenchData data = stack.getOrDefault(ModItemDataComponents.WRENCH_DATA, new WrenchData(WrenchMode.SIDE));
        WrenchMode currentMode = data.mode();
        
        // モードに応じた翻訳キーを選択
        Component modeName;
        if (currentMode == WrenchMode.SIDE) {
            modeName = Component.translatable(MMLang.Tooltip.Item.MagicalMechanics.Wrench.SIDE).withStyle(ChatFormatting.AQUA);
        } else {
            modeName = Component.translatable(MMLang.Tooltip.Item.MagicalMechanics.Wrench.CORE).withStyle(ChatFormatting.AQUA);
        }
        
        // モードの組み立て
        tooltip.add(Component.translatable(MMLang.Tooltip.Item.MagicalMechanics.Wrench.MODE, modeName)
                .withStyle(ChatFormatting.GRAY));
    }
}
