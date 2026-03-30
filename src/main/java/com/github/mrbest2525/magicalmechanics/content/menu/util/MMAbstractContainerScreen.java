package com.github.mrbest2525.magicalmechanics.content.menu.util;

import com.github.mrbest2525.magicalmechanics.MagicalMechanics;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

public abstract class MMAbstractContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    protected final GuiLayout GUI_LAYOUT;
    // 背景設定
    private final ResourceLocation INVENTORY_SLOT_TEXTURE = ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "textures/gui/inventory_slot/normal_inventory_slot.png");
    
    // 背景の中心を配置する画面比率
    private final float inventoryX = 0.5f;
    private final float inventoryY = 0.65f;
    private final int inventoryOffsetX = -80;
    private final int inventoryOffsetY = -37;
    
    private float containerOffsetX = 0;
    private float containerOffsetY = 0;
    
    // HUBの表示設定
    boolean originalOption;
    
    public MMAbstractContainerScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.GUI_LAYOUT = new GuiLayout(this.width, this.height);
        
        containerOffsetX = -((float) this.width / 2 - (float) this.imageWidth / 2);
        containerOffsetY = -((float) this.height / 2 - (float) this.imageHeight / 2);
        
        // 描画前にHUDを隠す設定（バニラや他MODの構成に依存する場合あり）
        originalOption = Minecraft.getInstance().options.hideGui;
        Minecraft.getInstance().options.hideGui = true;
    }
    
    @Override
    public void onClose() {
        // 描画後に戻す
        Minecraft.getInstance().options.hideGui = originalOption;
        
        super.onClose();
    }
    
    @Override
    protected void init() {
        super.init();
        this.GUI_LAYOUT.updateSize(this.width, this.height);
        
        containerOffsetX = -((float) this.width / 2 - (float) this.imageWidth / 2);
        containerOffsetY = -((float) this.height / 2 - (float) this.imageHeight / 2);
    }
    
    protected void setImageSize(int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        
        containerOffsetX = -((float) this.width / 2 - (float) this.imageWidth / 2);
        containerOffsetY = -((float) this.height / 2 - (float) this.imageHeight / 2);
    }
    
    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        
        // 背景描画
        super.render(gui, mouseX, mouseY, partialTick);
        
        this.renderTooltip(gui, mouseX, mouseY);
        
    }
    
    @Override
    protected void renderBg(@NotNull GuiGraphics gui, float partialTick, int mouseX, int mouseY) {
        gui.pose().pushPose();
        gui.pose().translate((int) GUI_LAYOUT.getPointX(inventoryX), (int) GUI_LAYOUT.getPointY(inventoryY), 0);
        gui.pose().translate(-110, -83, 0);
        
        RenderSystem.enableBlend();
        gui.blit(INVENTORY_SLOT_TEXTURE, 0, 0, 0, 0, 220, 166, 220, 166);
        RenderSystem.disableBlend();
        
        gui.pose().popPose();
    }
    
    @Override
    protected boolean isHovering(@NotNull Slot slot, double mouseX, double mouseY) {
        if (slot.index < 36) {
            // 1. renderSlotで「スロット(0,0)」として定義した絶対座標を算出
            float baseX = getSlotBaseX();
            float baseY = getSlotBaseY();
            
            // 2. そのスロットの左上角の絶対座標
            float slotLeft = baseX + slot.x;
            float slotTop = baseY + slot.y;
            
            // 3. マウスが「スロットの左上」から「16px以内」にいるか判定
            return mouseX >= slotLeft && mouseX < (slotLeft + 16)
                    && mouseY >= slotTop  && mouseY < (slotTop + 16);
            
        }
        
        return super.isHovering(slot, mouseX, mouseY);
    }
    
    @Override public void renderTransparentBackground(@NotNull GuiGraphics gui) {}
    
    public abstract void addRenderSlot(@NotNull GuiGraphics guiGraphics, @NotNull Slot slot);
    
    @Override public void renderSlot(@NotNull GuiGraphics guiGraphics, @NotNull Slot slot) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(containerOffsetX, containerOffsetY, 0);
        if (slot.index < 36) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate((int) getSlotBaseX(), (int) getSlotBaseY(), 0);
            super.renderSlot(guiGraphics, slot);
            guiGraphics.pose().popPose();
        } else {
            addRenderSlot(guiGraphics, slot);
        }
        guiGraphics.pose().popPose();
    }
    
    protected abstract void addRenderSlotHighlight(@NotNull GuiGraphics guiGraphics, @NotNull Slot slot, int mouseX, int mouseY, float partialTick);
    
    @Override protected void renderSlotHighlight(@NotNull GuiGraphics guiGraphics, @NotNull Slot slot, int mouseX, int mouseY, float partialTick) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(containerOffsetX, containerOffsetY, 0);
        guiGraphics.pose().translate((int) GUI_LAYOUT.getPointX(inventoryX), (int) GUI_LAYOUT.getPointY(inventoryY), 0);
        if (slot.index < 36) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(inventoryOffsetX, inventoryOffsetY, 0);
            super.renderSlotHighlight(guiGraphics, slot, mouseX, mouseY, partialTick);
            guiGraphics.pose().popPose();
        } else {
            addRenderSlotHighlight(guiGraphics, slot, mouseX, mouseY, partialTick);
        }
        guiGraphics.pose().popPose();
    }
    
    @Override protected void renderLabels(@NotNull GuiGraphics gui, int mx, int my) {}
    
    
    private float getSlotBaseX() {
        return GUI_LAYOUT.getPointX(inventoryX) + inventoryOffsetX;
    }
    
    private float getSlotBaseY() {
        return GUI_LAYOUT.getPointY(inventoryY) + inventoryOffsetY;
    }
}
