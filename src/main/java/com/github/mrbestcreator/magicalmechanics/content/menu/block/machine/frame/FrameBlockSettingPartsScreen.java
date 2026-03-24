package com.github.mrbestcreator.magicalmechanics.content.menu.block.machine.frame;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import com.github.mrbestcreator.magicalmechanics.content.menu.util.GuiLayout;
import com.github.mrbestcreator.magicalmechanics.content.menu.util.PlayerInventoryUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

public class FrameBlockSettingPartsScreen extends AbstractContainerScreen<FrameBlockSettingPartsMenu> {
    
    private final GuiLayout GUI_LAYOUT = new GuiLayout(this.width, this.height);
    
    private final PlayerInventoryUtil.PlayerInventory playerInventory;
    
    // ========================================
    // Screen Settings
    // ========================================
    private final float inventoryX = 0.5f;
    private final float inventoryY = 0.65f;
    
    // ========================================
    // Screen Textures
    // ========================================
    private final ResourceLocation INVENTORY_SLOT_TEXTURE = ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "textures/gui/inventory_slot/normal_inventory_slot.png");
    
    public FrameBlockSettingPartsScreen(FrameBlockSettingPartsMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        
        this.playerInventory = new PlayerInventoryUtil.PlayerInventory(GUI_LAYOUT.getPointX(inventoryX), GUI_LAYOUT.getPointY(inventoryY));
    }
    
    @Override
    protected void init() {
        this.imageWidth = this.width;
        this.imageHeight = this.height;
        super.init();
        GUI_LAYOUT.updateSize(this.width, this.height);
        playerInventory.update(GUI_LAYOUT.getPointX(inventoryX), GUI_LAYOUT.getPointY(inventoryY));
    }
    
    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
    
    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBg(guiGraphics, partialTick, mouseX, mouseY);
    }
    
    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float v, int i, int i1) {
        // inventoryの描画
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(GUI_LAYOUT.getPointX(inventoryX), GUI_LAYOUT.getPointY(inventoryY), 0);
        guiGraphics.pose().translate(-110, -83, 0);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(INVENTORY_SLOT_TEXTURE, 0, 0, 0, 0, 220, 166, 220, 166);
        RenderSystem.disableBlend();
        guiGraphics.pose().popPose();
    }
    
    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }
    
    @Override
    protected void renderSlot(@NotNull GuiGraphics guiGraphics, @NotNull Slot slot) {
        if (slot.index <= 35) {
            guiGraphics.pose().pushPose();
            playerInventory.renderTranslate(guiGraphics, slot);
            super.renderSlot(guiGraphics, slot);
            guiGraphics.pose().popPose();
        } else {
            super.renderSlot(guiGraphics, slot);
        }
    }
    
    @Override
    public boolean isHovering(@NotNull Slot slot, double mouseX, double mouseY) {
        float finalX, finalY;
        
        if (slot.index <= 35) {
            return playerInventory.isHovering(slot, mouseX, mouseY);
        } else {
            // その他（デフォルト）
            return super.isHovering(slot.x, slot.y, 16, 16, mouseX, mouseY);
        }
        
        // 判定範囲の計算 (中心 finalX, finalY から、サイズ 16 * scale の半分ずつ広げる)
//        float halfSize = (16.0f) / 2.0f;

//        return  this.isHovering((int) (finalX - halfSize), (int) (finalY - halfSize), (int) (halfSize * 2), (int) (halfSize * 2), mouseX, mouseY);
    }
    
    @Override
    protected void renderSlotHighlight(@NotNull GuiGraphics guiGraphics, @NotNull Slot slot, int mouseX, int mouseY, float partialTick) {
        // 自分の判定ロジックが true のときだけ描画
        if (this.isHovering(slot, mouseX, mouseY)) {
            
            // --- ここで isHovering と同じ finalX, finalY, currentScale を算出 ---
            // (コードの重複を避けるなら、座標算出部分をメソッドに切り出すと楽です)
            float finalX, finalY;
            float currentScale;
            
            if (slot.index <= 35) {
                playerInventory.renderSlotHighlight(guiGraphics, slot, mouseX, mouseY, partialTick);
                return;
            } else {
                // その他（デフォルト）
                super.renderSlotHighlight(guiGraphics, slot, mouseX, mouseY, partialTick);
                return;
            }

//            float size = 16.0f;

//            guiGraphics.pose().pushPose();
//            guiGraphics.pose().translate(0, 0, 100); // アイテムより手前に描画
//            if (slot.index >= 36 && slot.index <= 37) {
//                guiGraphics.pose().translate(-size / 2, -size / 2, 0);
//            }
//
//            guiGraphics.fill(RenderType.guiOverlay(), (int) finalX, (int) finalY, (int) (finalX + size), (int) (finalY + size), 0x80FFFFFF);
//
//
//            guiGraphics.pose().popPose();
        }
    }
}
