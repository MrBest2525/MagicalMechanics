package com.github.mrbest2525.magicalmechanics.content.menu.block.machine.frame;

import com.github.mrbest2525.magicalmechanics.content.menu.util.MMAbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

public class FrameBlockSettingPartsScreen extends MMAbstractContainerScreen<FrameBlockSettingPartsMenu> {
    
    public FrameBlockSettingPartsScreen(FrameBlockSettingPartsMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }
    
    @Override
    public void addRenderSlot(@NotNull GuiGraphics guiGraphics, @NotNull Slot slot) {

    }
    
    @Override
    protected void addRenderSlotHighlight(@NotNull GuiGraphics guiGraphics, @NotNull Slot slot, int mouseX, int mouseY, float partialTick) {
    
    }
}
