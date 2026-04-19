package io.github.mrbest2525.magicalmechanics.content.menu.util;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayerInventoryUtil {
    public static void setPlayerInventorySlot(AbstractContainerMenu menu, Inventory playerInventory) {
        // 1. プレイヤーインベントリ (Index 0 ~ 26) & ホットバー (Index 27 ~ 35)
        for (int i = 9; i <= 35; i++) {
            menu.addSlot(new Slot(playerInventory, i, 0, 0));
        }
        for (int i = 0; i <= 8; i++) {
            menu.addSlot(new Slot(playerInventory, i, 0, 0));
        }
    }
    
    public static boolean moveWithinPlayerInventory(AbstractContainerMenu menu, ItemStack stack, int index) {
        if (index < 27) { // メインインベントリ -> ホットバー
            return menu.moveItemStackTo(stack, 27, 36, false);
        } else { // ホットバー -> メインインベントリ
            return menu.moveItemStackTo(stack, 0, 27, false);
        }
    }
    
    public record InventorySlot(float slotX, float slotY) {}
    
    public static class PlayerInventory{
        private final List<InventorySlot> slotList = new ArrayList<>();
        
        public List<InventorySlot> getSlotList(){
            return List.copyOf(slotList);
        }
        
        public InventorySlot getSlot(int index) {
            return slotList.get(index);
        }
        
        public PlayerInventory(float centerX, float centerY) {
            update(centerX, centerY);
        }
        
        public void update(float centerX, float centerY) {
            init(centerX, centerY);
        }
        
        private void init(float centerX, float centerY) {
            slotList.clear();
            for (int i = 0; i <= 35; i++) {
                float x, y;
                if (i <= 26) {
                    // --- 1. メインインベントリ ---
                    x = centerX + (-80 + (18 * (i % 9)));
                    y = centerY + (-37 + (18 * (i / 9)));
                } else {
                    // --- 2. ホットバー ---
                    x = centerX + (-80 + (18 * (i % 9)));
                    y = centerY + (-37 + 18 * 3 + 4);
                }
                slotList.add(new InventorySlot(x, y));
            }
        }
        
        public void renderTranslate(@NotNull GuiGraphics guiGraphics, @NotNull Slot slot) {
            if (slot.index >= 0 && slot.index <= 35) {
                guiGraphics.pose().translate(slotList.get(slot.index).slotX, slotList.get(slot.index).slotY, 0);
            }
        }
        
        public boolean isHovering(@NotNull Slot slot, double mouseX, double mouseY) {
            if (slot.index >= 0 && slot.index <= 35) {
                return mouseX >= slotList.get(slot.index).slotX && mouseX < slotList.get(slot.index).slotX + 16 && mouseY >= slotList.get(slot.index).slotY && mouseY < slotList.get(slot.index).slotY + 16;
            }
            return false;
        }
        
        public void renderSlotHighlight(@NotNull GuiGraphics guiGraphics, @NotNull Slot slot, int mouseX, int mouseY, float partialTick) {
            if (slot.index >= 0 && slot.index <= 35) {
                int size = 16;
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(0, 0, 100);
                guiGraphics.fill(RenderType.guiOverlay(), (int)slotList.get(slot.index).slotX, (int)slotList.get(slot.index).slotY, (int)(slotList.get(slot.index).slotX + size), (int)(slotList.get(slot.index).slotY + size), 0x80FFFFFF);
                guiGraphics.pose().popPose();
            }
        }
    }
}
