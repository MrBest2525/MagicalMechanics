package com.github.mrbestcreator.magicalmechanics.content.menu.item.machineparts.furnacecore;

import com.github.mrbestcreator.magicalmechanics.content.menu.util.GuiLayout;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class FurnaceCorePartsScreen extends AbstractContainerScreen<FurnaceCorePartsMenu> {
    
    private final GuiLayout GUI_LAYOUT = new GuiLayout(this.width, this.height);
    
    public FurnaceCorePartsScreen(FurnaceCorePartsMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }
    
    @Override
    protected void init() {
        super.init();
        GUI_LAYOUT.updateSize(this.width, this.height);
    }
    
    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderables.forEach(r -> r.render(guiGraphics, mouseX, mouseY, partialTick));
        this.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        
    }
    
    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float v, int i, int i1) {
        // TODO 四角いパーティクル的なので(■)燃えてる時に炎があがっていくような動的Screenにしたい
    }
    
    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }
    
    
}
