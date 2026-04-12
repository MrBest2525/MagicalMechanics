package com.github.mrbest2525.magicalmechanics.content.menu.util;

import com.github.mrbest2525.magicalmechanics.util.MMTextures;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

import static com.github.mrbest2525.magicalmechanics.util.MMTextures.GUI.NORMAL_INVENTORY;

public abstract class MMAbstractContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    protected final GuiLayout GUI_LAYOUT;
    
    // プレイヤーインベントリの配置基準
    private final float inventoryAnchorX = 0.5f;
    private final float inventoryAnchorY = 0.65f;
    protected final int inventoryOffsetX = -80;
    protected final int inventoryOffsetY = -37;
    
    public MMAbstractContainerScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.GUI_LAYOUT = new GuiLayout(this.width, this.height);
    }
    
    @Override
    protected void init() {
        super.init();
        this.GUI_LAYOUT.updateSize(this.width, this.height);
        
        // 重要：バニラの「基準点」を、僕たちが決めたい「インベントリの左上」に設定する
        // これで super.renderSlot(slot) を呼ぶだけで、(leftPos + slot.x) にアイテムが描画される
        this.leftPos = (int) GUI_LAYOUT.getPointX(inventoryAnchorX) + inventoryOffsetX;
        this.topPos = (int) GUI_LAYOUT.getPointY(inventoryAnchorY) + inventoryOffsetY;
    }
    
    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        super.render(gui, mouseX, mouseY, partialTick);
        this.renderTooltip(gui, mouseX, mouseY);
    }
    
    @Override
    protected void renderBg(@NotNull GuiGraphics gui, float partialTick, int mouseX, int mouseY) {
        // 背景画像(blit)は、leftPosを基準に描画
        gui.pose().pushPose();
        gui.pose().translate(this.leftPos, this.topPos, 0);
        
        // 元の (-110, -83) は、スロット(0,0)から見た背景のズレ
        // inventoryOffsetX(-80) 基準なら、そこからさらに調整が必要
        int bgOffsetX = -30; // 例: -110 - (-80)
        int bgOffsetY = -46; // 例: -83 - (-37)
        
        RenderSystem.enableBlend();
        MMTextures.blitAsset(gui, NORMAL_INVENTORY, bgOffsetX, bgOffsetY);
        RenderSystem.disableBlend();
        gui.pose().popPose();
    }
    
    public void renderSlotBase(@NotNull GuiGraphics gui, @NotNull Slot slot) {
        super.renderSlot(gui, slot);
    }
    
    @Override
    public void renderSlot(@NotNull GuiGraphics gui, @NotNull Slot slot) {
        if (slot.index < 36) {
            // プレイヤーの持ち物は、init()で設定した leftPos に従ってバニラが正しく描画する
            super.renderSlot(gui, slot);
        } else {
            // 特殊スロット（index 36〜）は各Screenで自由に料理する
            addRenderSlot(gui, slot);
        }
    }
    
    protected void renderSlotHighlightBase(@NotNull GuiGraphics gui, @NotNull Slot slot, int mouseX, int mouseY, float partialTick) {
        super.renderSlotHighlight(gui, slot, mouseX, mouseY, partialTick);
    }
    
    @Override
    protected void renderSlotHighlight(@NotNull GuiGraphics gui, @NotNull Slot slot, int mouseX, int mouseY, float partialTick) {
        if (slot.index < 36) {
            super.renderSlotHighlight(gui, slot, mouseX, mouseY, partialTick);
        } else {
            addRenderSlotHighlight(gui, slot, mouseX, mouseY, partialTick);
        }
    }
    
    public abstract void addRenderSlot(@NotNull GuiGraphics gui, @NotNull Slot slot);
    protected abstract void addRenderSlotHighlight(@NotNull GuiGraphics gui, @NotNull Slot slot, int mouseX, int mouseY, float partialTick);
    
    @Override protected void renderLabels(@NotNull GuiGraphics gui, int mx, int my) {}
    @Override public void renderTransparentBackground(@NotNull GuiGraphics gui) {}
    
    // ヘルパー：バニラが勝手に足す「leftPos + slot.x」を打ち消して、絶対座標 targetX に移動させる
    protected void moveToAbsolute(@NotNull GuiGraphics gui, @NotNull Slot slot, float targetX, float targetY) {
        float diffX = targetX - (this.leftPos + slot.x);
        float diffY = targetY - (this.topPos + slot.y);
        gui.pose().translate(diffX, diffY, 0);
    }
}
