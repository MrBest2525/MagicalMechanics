package com.github.mrbestcreator.magicalmechanics.content.menu.item.machineparts.furnaceside;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class FurnaceSidePartsScreen extends AbstractContainerScreen<FurnaceSidePartsMenu> {
    public FurnaceSidePartsScreen(FurnaceSidePartsMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }
    
    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float v, int i, int i1) {
    
    }
}
