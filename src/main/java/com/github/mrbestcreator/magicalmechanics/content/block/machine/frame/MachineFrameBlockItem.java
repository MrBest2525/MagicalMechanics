package com.github.mrbestcreator.magicalmechanics.content.block.machine.frame;

import com.github.mrbestcreator.magicalmechanics.util.MMLang;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MachineFrameBlockItem extends BlockItem {
    public MachineFrameBlockItem(Block block, Properties properties) {
        super(block, properties);
    }
    
    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        
        CustomData data = itemStack.get(DataComponents.BLOCK_ENTITY_DATA);
        
        if (data != null) {
            CompoundTag tag = data.copyTag();
            if (Screen.hasShiftDown()) {
                if (tag.contains("Parts", Tag.TAG_COMPOUND)) {
                    CompoundTag partsTag = tag.getCompound("Parts");
                    
                    tooltip.add(Component.translatable(MMLang.Tooltip.Item.MagicalMechanics.MachineFrame.CONTENTS).withStyle(ChatFormatting.GRAY));
                    
                    String[] slots = {"CORE", "SIDE"};
                    for (String slotKey : slots) {
                        if (partsTag.contains(slotKey)) {
                            CompoundTag itemTag = partsTag.getCompound(slotKey);
                            
                            HolderLookup.Provider registries = tooltipContext.registries();
                            if (registries != null) {
                                ItemStack.parse(registries, itemTag).ifPresent(partStack -> {
                                    MutableComponent line = Component.translatable(getTranslate(slotKey), partStack.getHoverName().copy())
                                            .withStyle(ChatFormatting.WHITE);
                                    
                                    tooltip.add(line);
                                });
                            }
                        }
                    }
                }
            } else {
                tooltip.add(Component.translatable(MMLang.Tooltip.Item.MagicalMechanics.MachineFrame.SHIFT_FOR_INFO).withStyle(ChatFormatting.YELLOW));
            }
        }
        
        super.appendHoverText(itemStack, tooltipContext, tooltip, tooltipFlag);
    }
    
    private String getTranslate(String slot) {
        return switch (slot) {
            case "CORE" -> MMLang.Tooltip.Item.MagicalMechanics.MachineFrame.CORE_SLOT;
            case "SIDE" -> MMLang.Tooltip.Item.MagicalMechanics.MachineFrame.SIDE_SLOT;
            default -> "";
        };
    }
}
