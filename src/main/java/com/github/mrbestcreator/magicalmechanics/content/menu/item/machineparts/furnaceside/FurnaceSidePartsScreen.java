package com.github.mrbestcreator.magicalmechanics.content.menu.item.machineparts.furnaceside;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.side.FurnaceSideInstance;
import com.github.mrbestcreator.magicalmechanics.content.menu.util.GuiLayout;
import com.github.mrbestcreator.magicalmechanics.content.menu.util.PlayerInventoryUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FurnaceSidePartsScreen extends AbstractContainerScreen<FurnaceSidePartsMenu> {
    
    private final GuiLayout GUI_LAYOUT = new GuiLayout(this.width, this.height);
    
    private final ResourceLocation NORMAL_SLOT_1 = ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "textures/gui/inventory_slot/normal_slot1.png");
    private final ResourceLocation FIRE_1 = ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "textures/gui/item/furnace_side/fire1.png");
    private final ResourceLocation WOK_1 = ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "textures/gui/item/furnace_side/wok1.png");
    
    private final ResourceLocation INVENTORY_SLOT_TEXTURE = ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "textures/gui/inventory_slot/normal_inventory_slot.png");
    
    private final float inputSlotX = 0.3f;
    private final float inputSlotY = 0.3f;
    
    private final float outputSlotX = 0.7f;
    private final float outputSlotY = 0.3f;
    
    private final float cookingItemX = 0.5f;
    private final float cookingItemY = 0.2f;
    private final float cookingItemScale = 10f;
    
    private final float fireX = 0.5f;
    private final float fireY = 0.4f;
    private final float fireScale = 5f;
    
    private final float inventoryX = 0.5f;
    private final float inventoryY = 0.65f;
    private final PlayerInventoryUtil.PlayerInventory playerInventory;
    
    public FurnaceSidePartsScreen(FurnaceSidePartsMenu menu, Inventory playerInventory, Component title) {
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
        
        // inputSlotの描画
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(GUI_LAYOUT.getPointX(inputSlotX), GUI_LAYOUT.getPointY(inputSlotY), 0);
        guiGraphics.pose().translate(-10, -10, 0);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(NORMAL_SLOT_1, 0, 0, 0, 0, 20, 20, 20, 20);
        RenderSystem.disableBlend();
        guiGraphics.pose().popPose();
        
        // outputSlotの描画
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(GUI_LAYOUT.getPointX(outputSlotX), GUI_LAYOUT.getPointY(outputSlotY), 0);
        guiGraphics.pose().translate(-10, -10, 0);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(NORMAL_SLOT_1, 0, 0, 0, 0, 20, 20, 20, 20);
        RenderSystem.disableBlend();
        guiGraphics.pose().popPose();
        
        // 焼いている物の表示
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(GUI_LAYOUT.getPointX(cookingItemX), GUI_LAYOUT.getPointY(cookingItemY), 0);
        float scale = (float) GUI_LAYOUT.getScale(cookingItemScale);
        guiGraphics.pose().scale(scale, scale, scale);
        guiGraphics.pose().translate(-8, -8, 0);
        ItemStack cookingItem = ItemStack.EMPTY;
        if (this.menu.blockEntity.sideInstance instanceof FurnaceSideInstance furnaceSideInstance) {
            cookingItem = furnaceSideInstance.getCookingItem();
        }
        guiGraphics.renderFakeItem(cookingItem, 0, 0);
        guiGraphics.pose().popPose();
        
        // 炎の表示
        float x = GUI_LAYOUT.getPointX(fireX);
        float y = GUI_LAYOUT.getPointY(fireY);
        scale = (float) GUI_LAYOUT.getScale(fireScale);
        for (int j = 1; j < 5; j++) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x, y, 0);
            guiGraphics.pose().translate(-(16 * scale * (j - 2)), 0, 0);
            guiGraphics.pose().scale(scale, scale, scale);
            guiGraphics.pose().translate(0, -8, 0);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            guiGraphics.blit(FIRE_1, 0, 0, 0, 0, 16, 16, 16, 16);
            RenderSystem.disableBlend();
            guiGraphics.pose().popPose();
        }
        
        // 中華鍋？の表示
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(GUI_LAYOUT.getPointX(cookingItemX), GUI_LAYOUT.getPointY(cookingItemY), 0);
        scale = (float) GUI_LAYOUT.getScale(cookingItemScale);
        guiGraphics.pose().scale(scale * 2, scale * 2, 0);
        guiGraphics.pose().translate(-8, -4, 0);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(WOK_1, 0, 0, 0, 0, 16, 16, 16, 16);
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
        } else if (slot.index == 36) {
            float targetX = GUI_LAYOUT.getPointX(inputSlotX);
            float targetY = GUI_LAYOUT.getPointY(inputSlotY);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(targetX, targetY, 0);
            guiGraphics.pose().translate(-8, -8, 0);
            super.renderSlot(guiGraphics, slot);
            guiGraphics.pose().popPose();
        } else if (slot.index == 37) {
            float targetX = GUI_LAYOUT.getPointX(outputSlotX);
            float targetY = GUI_LAYOUT.getPointY(outputSlotY);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(targetX, targetY, 0);
            guiGraphics.pose().translate(-8, -8, 0);
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
        } else if (slot.index == 36) {
            finalX = GUI_LAYOUT.getPointX(inputSlotX);
            finalY = GUI_LAYOUT.getPointY(inputSlotY);
        } else if (slot.index == 37) {
            finalX = GUI_LAYOUT.getPointX(outputSlotX);
            finalY = GUI_LAYOUT.getPointY(outputSlotY);
        } else {
            // その他（デフォルト）
            return super.isHovering(slot.x, slot.y, 16, 16, mouseX, mouseY);
        }
        
        // 判定範囲の計算 (中心 finalX, finalY から、サイズ 16 * scale の半分ずつ広げる)
        float halfSize = (16.0f) / 2.0f;
        
        return  this.isHovering((int) (finalX - halfSize), (int) (finalY - halfSize), (int) (halfSize * 2), (int) (halfSize * 2), mouseX, mouseY);
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
            } else if (slot.index == 36) {
                finalX = GUI_LAYOUT.getPointX(inputSlotX);
                finalY = GUI_LAYOUT.getPointY(inputSlotY);
            } else if (slot.index == 37) {
                finalX = GUI_LAYOUT.getPointX(outputSlotX);
                finalY = GUI_LAYOUT.getPointY(outputSlotY);
            } else {
                // その他（デフォルト）
                super.renderSlotHighlight(guiGraphics, slot, mouseX, mouseY, partialTick);
                return;
            }
            
            float size = 16.0f;
            
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, 0, 100); // アイテムより手前に描画
            if (slot.index >= 36 && slot.index <= 37) {
                guiGraphics.pose().translate(-size / 2, -size / 2, 0);
            }
            
            guiGraphics.fill(RenderType.guiOverlay(), (int)finalX, (int)finalY, (int)(finalX + size), (int)(finalY + size), 0x80FFFFFF);
            
            
            guiGraphics.pose().popPose();
        }
    }
}
