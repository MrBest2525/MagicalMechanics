package com.github.mrbest2525.magicalmechanics.content.menu.item.machineparts.energycore;

import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.core.energy.EnergyCoreInstance;
import com.github.mrbest2525.magicalmechanics.content.menu.util.GuiLayout;
import com.github.mrbest2525.magicalmechanics.content.menu.util.MMAbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

public class EnergyCorePartScreen extends MMAbstractContainerScreen<EnergyCorePartMenu> {
    
    private final GuiLayout GUI_LAYOUT;
    
    public EnergyCorePartScreen(EnergyCorePartMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        GUI_LAYOUT = new GuiLayout(this.width, this.height);
    }
    
    @Override
    protected void init() {
        super.init();
        GUI_LAYOUT.updateSize(this.width, this.height);
    }
    
    @Override
    public void addRenderSlot(@NotNull GuiGraphics gui, @NotNull Slot slot) {
    
    }
    
    @Override
    protected void addRenderSlotHighlight(@NotNull GuiGraphics gui, @NotNull Slot slot, int mouseX, int mouseY, float partialTick) {
    
    }
    
    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        
        
        
        
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(GUI_LAYOUT.getPointX(0.5), GUI_LAYOUT.getPointY(0.25), 0);
        if (this.menu.getBlockEntity().coreInstance instanceof EnergyCoreInstance energyCore) {
            guiGraphics.drawString(font, energyCore.getEnergy().toString(), 0, 0, 0xFFFFFFFF, true);
        }
        guiGraphics.pose().popPose();
    }
}
