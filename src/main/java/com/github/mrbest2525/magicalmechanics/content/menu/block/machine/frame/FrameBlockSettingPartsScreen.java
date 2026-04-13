package com.github.mrbest2525.magicalmechanics.content.menu.block.machine.frame;

import com.github.mrbest2525.magicalmechanics.MagicalMechanics;
import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.SettingPartsManager;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.SettingParts;
import com.github.mrbest2525.magicalmechanics.content.menu.util.MMAbstractContainerScreen;
import com.github.mrbest2525.magicalmechanics.network.packet.menu.machineframe.settingpert.DeselectPartPayload;
import com.github.mrbest2525.magicalmechanics.network.packet.menu.machineframe.settingpert.SelectPartPayload;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FrameBlockSettingPartsScreen extends MMAbstractContainerScreen<FrameBlockSettingPartsMenu> {
    
    ResourceLocation PARTS_SLOT_TEXTURE = ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "textures/gui/block/frame/slot.png");
    
    private Map<UUID, SettingPartsManager.PartItemStack> partsList = Map.of();
    private List<Map.Entry<UUID, SettingPartsManager.PartItemStack>> sortedPartsList = new ArrayList<>();
    
    private final double inputX = 0.3, inputY = 0.6;
    private final double outputX = 0.7, outputY = 0.6;
    
    private final double scrollX = 0.5, scrollY = 0.1;
    private final double scrollWidth = 0.5;
    private float scrollAmount = 0.0f;
    private float targetScrollAmount = 0.0f;
    private static final int ITEM_W = 22; // アイテム1つ分の横幅 (16 + 左右3pxずつ)
    private static final int LIST_H = 22; // アイテムの高さ (16 + 上下3pxずつ)
    
    private final double selectItemX = 0.5, selectItemY = 0.35;
    private final float selectItemScale = 10;
    
    // パネルのサイズ
    private final double panelW = 0.3, panelH = 0.2;
    // 左側パネル
    private int lastLeftContentHeight = 0;
    private float leftScrollAmount = 0.0f;
    private final double detailX = 0.3, detailY = 0.35;
    // 右側パネル
    private float infoScrollAmount = 0.0f;
    private final double infoX = 0.7, infoY = 0.25;
    
    private final boolean isHideHub;
    
    public FrameBlockSettingPartsScreen(FrameBlockSettingPartsMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.isHideHub = Minecraft.getInstance().options.hideGui;
        Minecraft.getInstance().options.hideGui = true;
    }
    
    @Override
    public void onClose() {
        Minecraft.getInstance().options.hideGui = isHideHub;
        super.onClose();
    }
    
    /**
     * パケットハンドラーから呼ばれる更新メソッド
     */
    public void updatePartsList(Map<UUID, SettingPartsManager.PartItemStack> newParts) {
        this.partsList = newParts;
        
        // ⭐ IDのアルファベット順でソート
        this.sortedPartsList = newParts.entrySet().stream()
                .sorted((e1, e2) -> {
                    // アイテムのIDを取得 (例: "minecraft:iron_ingot")
                    String id1 = BuiltInRegistries.ITEM.getKey(e1.getValue().getItemStack().getItem()).toString();
                    String id2 = BuiltInRegistries.ITEM.getKey(e2.getValue().getItemStack().getItem()).toString();
                    return id1.compareTo(id2); // 辞書順
                })
                .toList();
        
        // スクロールの再計算など
//        this.rebuildScrollList();
    }
    
    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        
        // ⭐ 目標値に向けて現在の値を少しずつ近づける (0.2f は速さ。0.1fだとよりゆっくり)
        if (Math.abs(scrollAmount - targetScrollAmount) > 0.001f) {
            scrollAmount = net.minecraft.util.Mth.lerp(0.2f, scrollAmount, targetScrollAmount);
        } else {
            scrollAmount = targetScrollAmount;
        }
        
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        
        var hovered = getHoveredPart(mouseX, mouseY);
        if (hovered != null) {
            ItemStack stack = hovered.getValue().getItemStack();
            // アイテム標準のツールチップ（名前、エンチャント、MOD名など）を表示
            guiGraphics.renderTooltip(this.font, stack, mouseX, mouseY);
        }
    }
    
    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        
        // --- 1. レイアウト基本情報の計算 ---
        int centerX = (int) GUI_LAYOUT.getPointX(scrollX);
        int centerY = (int) GUI_LAYOUT.getPointY(scrollY);
        int maxWidth = (int) GUI_LAYOUT.getPointX(scrollWidth);
        
        int startX = centerX - (maxWidth / 2);
        int startY = centerY - (LIST_H / 2);
        
        // --- 2. リストアイテムの描画 (Scissorで範囲外をカット) ---
        guiGraphics.enableScissor(startX, startY, startX + maxWidth, startY + LIST_H);
        
        int totalCount = sortedPartsList.size();
        if (totalCount > 0) {
            float centerIdx = (totalCount - 1) / 2.0f;
            // スクロール可能範囲がある場合のみ補正
            float maxScrollRange = Math.max(0, totalCount - (maxWidth / (float)ITEM_W));
            float scrollOffset = (scrollAmount - 0.5f) * maxScrollRange;
            
            for (int i = 0; i < totalCount; i++) {
                var entry = sortedPartsList.get(i);
                float relativeIdx = (i - centerIdx) - scrollOffset;
                int itemX = (int) (centerX + (relativeIdx * ITEM_W) - (ITEM_W / 2.0f));
                
                // 画面外（Scissor内だけど計算上不要な範囲）はスキップ
                if (itemX + ITEM_W < startX || itemX > startX + maxWidth) continue;
                
                // アイコン描画 (左右3pxの余白を考慮)
                guiGraphics.renderItem(entry.getValue().getItemStack(), itemX + 3, startY + 3);
                
                // 個数描画 (小文字スケール)
                renderItemCount(guiGraphics, entry.getValue().getCount(), itemX, startY);
                
                // 選択中のハイライト (白枠)
                if (entry.getKey().equals(this.getMenu().getSelectedPartUuid())) {
                    guiGraphics.renderOutline(itemX, startY, ITEM_W, LIST_H, 0xFFFFFFFF);
                }
            }
        }
        guiGraphics.disableScissor();
        // --- 3. 装飾：左右の縦ライン (2px外側) ---
        int lineXLeft = startX - 2;
        int lineXRight = startX + maxWidth + 2;
        int centerY_Local = startY + (LIST_H / 2);
        int colorCenter = 0xFFFFFFFF;
        int colorEdge = 0x00FFFFFF;
        
        // 左ライン
        guiGraphics.fillGradient(lineXLeft, startY, lineXLeft + 1, centerY_Local, colorEdge, colorCenter);
        guiGraphics.fillGradient(lineXLeft, centerY_Local, lineXLeft + 1, startY + LIST_H, colorCenter, colorEdge);
        // 右ライン
        guiGraphics.fillGradient(lineXRight - 1, startY, lineXRight, centerY_Local, colorEdge, colorCenter);
        guiGraphics.fillGradient(lineXRight - 1, centerY_Local, lineXRight, startY + LIST_H, colorCenter, colorEdge);
        
        // --- 4. 装飾：上下の横ライン (pose回転を利用) ---
        int lineHalfW = (int) (maxWidth / 2.0);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(centerX, startY, 0);
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(90.0f));
        
        // 上の横線 (回転しているので見た目上は横に消える)
        guiGraphics.fillGradient(-1, 0, 0, lineHalfW, colorCenter, colorEdge);
        guiGraphics.fillGradient(-1, -lineHalfW, 0, 0, colorEdge, colorCenter);
        
        // 下の横線へ移動
        guiGraphics.pose().translate(LIST_H, 0, 0);
        guiGraphics.fillGradient(-1, 0, 0, lineHalfW, colorCenter, colorEdge);
        guiGraphics.fillGradient(-1, -lineHalfW, 0, 0, colorEdge, colorCenter);
        guiGraphics.pose().popPose();
        
        // --- 5. 中央の巨大アイコン表示 (バリア または 選択中アイテム) ---
        UUID selectedUuid = this.menu.getSelectedPartUuid();
        ItemStack selectItem = ItemStack.EMPTY;
        
        // 同期されているpartsListから現在のアイテムを特定
        if (selectedUuid != null && partsList.containsKey(selectedUuid)) {
            selectItem = partsList.get(selectedUuid).getItemStack();
        }
        
        // 未選択ならバリアを出す
        if (selectItem.isEmpty()) {
            selectItem = new ItemStack(Items.BARRIER);
        }
        
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(GUI_LAYOUT.getPointX(selectItemX), GUI_LAYOUT.getPointY(selectItemY), 0);
        float s = (float) GUI_LAYOUT.getScale(selectItemScale);
        guiGraphics.pose().scale(s, s, s);
        // 重なり順を考慮して手前に
        guiGraphics.pose().translate(0, 0, 100);
        // 中心揃えで描画
        guiGraphics.renderItem(selectItem, -8, -8);
        guiGraphics.pose().popPose();
        
        // 詳細パネルの描画 (一番手前に持ってくる)
        // 左側：詳細説明
        renderLeftPanel(guiGraphics, mouseX, mouseY, partialTick);
        
        // 右側：基本ステータス
        renderGenericPanel(guiGraphics, infoX, infoY, infoScrollAmount, "[ステータス]", "消費: 10 FE/t\n出力: 5.0 rad/s\n耐久: 100%\n................\n................\n....................\n................\n................\n....................\n................\n................\n....................\n................\n................\n....................\n................\n................\n....................\n................\n................\n....................\n................\n................\n....................\n................\n................\n....................\n................\n................\n....................\n................\n................\n....................\n................\n................\n....................\n");
    }
    
    // 読みやすくするために個数描画をメソッド化する例
    private void renderItemCount(GuiGraphics guiGraphics, int count, int itemX, int startY) {
        String countText = String.valueOf(count);
        int textWidth = font.width(countText);
        guiGraphics.pose().pushPose();
        float textScale = 0.5f;
        guiGraphics.pose().translate(itemX + (ITEM_W / 2.0f), startY + LIST_H - 5, 200);
        guiGraphics.pose().scale(textScale, textScale, 1.0f);
        guiGraphics.drawString(font, countText, -(textWidth / 2), 0, 0xFFFFFFFF, true);
        guiGraphics.pose().popPose();
    }
    
    private void renderLeftPanel(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // 1. GUI_LAYOUTからサイズと「中心座標」を取得
        int pW = (int) GUI_LAYOUT.getPointX(panelW);
        int pH = (int) GUI_LAYOUT.getPointY(panelH);
        int centerX = (int) GUI_LAYOUT.getPointX(detailX);
        int centerY = (int) GUI_LAYOUT.getPointY(detailY);
        
        // 2. 左上の開始座標を計算 (中心からサイズの半分を引く)
        int startX = centerX - (pW / 2);
        int startY = centerY - (pH / 2);
        
        // 3. スクロールの計算
        int scrollableRange = Math.max(0, this.lastLeftContentHeight - pH);
        float pixelOffset = this.leftScrollAmount * scrollableRange;
        
        // 4. Scissor (開始座標 startX, startY から pW, pH のサイズで切り抜く)
        guiGraphics.enableScissor(startX, startY, startX + pW, startY + pH);
        
        guiGraphics.pose().pushPose();
        
        // 5. 原点をパネルの左上に移動し、スクロール分だけ「上」にずらす
        guiGraphics.pose().translate(startX, startY - pixelOffset, 0);
        
        // 6. 選択中のアイテムを描画
        UUID selectedUuid = this.menu.getSelectedPartUuid();
        if (selectedUuid != null && partsList.containsKey(selectedUuid)) {
            ItemStack selectedStack = partsList.get(selectedUuid).getItemStack();
            if (selectedStack.getItem() instanceof SettingParts part) {
                // マウス座標をパネル内の相対座標（スクロール補正込み）に変換
                double relMouseX = mouseX - startX;
                double relMouseY = mouseY - startY + pixelOffset;
                
                this.lastLeftContentHeight = part.renderStaticInfo(
                        this,
                        guiGraphics,
                        pW,
                        pH,
                        relMouseX,
                        relMouseY,
                        partialTick
                );
            }
        }
        
        guiGraphics.pose().popPose();
        guiGraphics.disableScissor();
        
        // 枠線の描画 (中心基準で正しく描画されているか確認用)
        guiGraphics.fill(startX - 2, startY - 2, startX + pW + 2, startY + pH + 2, 0x88000000);
        guiGraphics.renderOutline(startX - 2, startY - 2, pW + 4, pH + 4, 0xFFFFFFFF);
    }
    
    private void renderGenericPanel(GuiGraphics guiGraphics, double relX, double relY, float scrollAmount, String title, String content) {
        int width = (int) GUI_LAYOUT.getPointX(panelW);
        int height = (int) GUI_LAYOUT.getPointY(panelH);
        int startX = (int) GUI_LAYOUT.getPointX(relX) - (width / 2);
        int startY = (int) GUI_LAYOUT.getPointY(relY);
        
        // 背景と枠
        guiGraphics.fill(startX - 2, startY - 2, startX + width + 2, startY + height + 2, 0x88000000);
        guiGraphics.renderOutline(startX - 2, startY - 2, width + 4, height + 4, 0xFFFFFFFF);
        
        guiGraphics.enableScissor(startX, startY, startX + width, startY + height);
        guiGraphics.pose().pushPose();
        
        var lines = font.split(Component.literal(content), width);
        int totalHeight = 12 + (lines.size() * 10);
        int scrollable = Math.max(0, totalHeight - height);
        float offset = scrollAmount * scrollable;
        
        guiGraphics.pose().translate(startX, startY - offset, 0);
        
        // タイトル
        guiGraphics.drawString(font, "§e" + title, 0, 0, 0xFFFFFFFF, true);
        
        // 本文
        int currY = 12;
        for (var line : lines) {
            guiGraphics.drawString(font, line, 0, currY, 0xAAAAAAAA, false);
            currY += 10;
        }
        
        guiGraphics.pose().popPose();
        guiGraphics.disableScissor();
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        // --- 1. 左パネル (Detail) の判定 ---
        int pW = (int) GUI_LAYOUT.getPointX(panelW);
        int pH = (int) GUI_LAYOUT.getPointY(panelH);
        int startX = (int) GUI_LAYOUT.getPointX(detailX) - (pW / 2);
        int startY = (int) GUI_LAYOUT.getPointY(detailY) - (pH / 2);
        
        if (mouseX >= startX && mouseX <= startX + pW && mouseY >= startY && mouseY <= startY + pH) {
            int scrollableRange = Math.max(0, this.lastLeftContentHeight - pH);
            if (scrollableRange > 0) {
                float scrollStep = 20.0f / (float)scrollableRange;
                this.leftScrollAmount = net.minecraft.util.Mth.clamp(this.leftScrollAmount - (float)scrollY * scrollStep, 0.0f, 1.0f);
            }
            // ⭐ 重要：スクロールの有無にかかわらず、マウスがここにあるならイベントを消費(吸い込む)
            return true;
        }
        
        // --- 2. 右パネル (Info) の判定 ---
        int iW = (int) GUI_LAYOUT.getPointX(panelW);
        int iH = (int) GUI_LAYOUT.getPointY(panelH);
        int iStartX = (int) GUI_LAYOUT.getPointX(infoX) - (iW / 2);
        int iStartY = (int) GUI_LAYOUT.getPointY(infoY) - (iH / 2);
        
        if (mouseX >= iStartX && mouseX <= iStartX + iW && mouseY >= iStartY && mouseY <= iStartY + iH) {
            // 右側も同様に、中身が短くてもスクロールを吸わせる
            this.infoScrollAmount = net.minecraft.util.Mth.clamp(this.infoScrollAmount - (float)scrollY * 0.1f, 0.0f, 1.0f);
            return true;
        }
        
        
        // --- メインリストのスクロール (既存の処理) ---
        int maxWidth = (int) GUI_LAYOUT.getPointX(scrollWidth);
        int totalContentWidth = sortedPartsList.size() * ITEM_W;
        
        if (totalContentWidth > maxWidth) {
            float delta = 30.0f / (float)(totalContentWidth - maxWidth);
            this.targetScrollAmount = net.minecraft.util.Mth.clamp(this.targetScrollAmount - (float)scrollY * delta, 0.0f, 1.0f);
            return true;
        }
        
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        var hovered = getHoveredPart(mouseX, mouseY);
        if (hovered != null) {
            UUID clickedUuid = hovered.getKey();
            UUID currentSelected = this.getMenu().getSelectedPartUuid();
            BlockPos pos = this.menu.getBlockEntity().getBlockPos();
            
            if (clickedUuid.equals(currentSelected)) {
                // 解除時はスクロール位置は変えなくてOK
                this.getMenu().setSelectedPart(null);
                PacketDistributor.sendToServer(new DeselectPartPayload(pos));
            } else {
                // ⭐ ここで自前の setSelectedPart(UUID) を呼ぶことでスクロールが走る
                this.setSelectedPart(clickedUuid);
                PacketDistributor.sendToServer(new SelectPartPayload(pos, clickedUuid));
            }
            
            this.minecraft.getSoundManager().play(net.minecraft.client.resources.sounds.SimpleSoundInstance.forUI(net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    public void setSelectedPart(@Nullable UUID uuid) {
        this.getMenu().setSelectedPart(uuid);
        
        if (uuid != null) {
            // 1. 選択されたアイテムがリストの何番目か探す
            int index = -1;
            for (int i = 0; i < sortedPartsList.size(); i++) {
                if (sortedPartsList.get(i).getKey().equals(uuid)) {
                    index = i;
                    break;
                }
            }
            
            // 2. インデックスが見つかったら、それが中央に来るように scrollAmount を計算
            if (index != -1) {
                int totalCount = sortedPartsList.size();
                int maxWidth = (int) GUI_LAYOUT.getPointX(scrollWidth);
                float itemsVisible = maxWidth / (float) ITEM_W;
                float maxScrollRange = totalCount - itemsVisible;
                
                if (maxScrollRange > 0) {
                    // 中央に来るべき相対位置を計算
                    // (index - 表示可能数の半分) が左端に来るべき index
                    float targetLeftIdx = index - (itemsVisible / 2.0f) + 0.5f;
                    
                    // 0.0 ~ 1.0 の範囲に変換
                    this.targetScrollAmount = net.minecraft.util.Mth.clamp(targetLeftIdx / maxScrollRange, 0.0f, 1.0f);
                }
            }
        }
    }
    
    private Map.Entry<UUID, SettingPartsManager.PartItemStack> getHoveredPart(double mouseX, double mouseY) {
        int centerX = (int) GUI_LAYOUT.getPointX(scrollX);
        int centerY = (int) GUI_LAYOUT.getPointY(scrollY);
        int maxWidth = (int) GUI_LAYOUT.getPointX(scrollWidth); // 描画と合わせる
        
        int startX = centerX - (maxWidth / 2);
        int startY = centerY - (LIST_H / 2);
        
        if (mouseY >= startY && mouseY < startY + LIST_H && mouseX >= startX && mouseX < startX + maxWidth) {
            int totalCount = sortedPartsList.size();
            if (totalCount == 0) return null;
            
            float centerIdx = (totalCount - 1) / 2.0f;
            float scrollOffset = (scrollAmount - 0.5f) * Math.max(0, totalCount - (maxWidth / (float)ITEM_W));
            
            // マウスのX座標から、リスト内の相対的な位置(index)を逆算
            double relativeMouseX = mouseX - centerX;
            int idx = (int) Math.floor((relativeMouseX / ITEM_W) + centerIdx + scrollOffset + 0.5f);
            
            if (idx >= 0 && idx < totalCount) {
                return sortedPartsList.get(idx);
            }
        }
        return null;
    }
    
    @Override
    public void addRenderSlot(@NotNull GuiGraphics guiGraphics, @NotNull Slot slot) {
        if (slot.index == 36) {
            float tx = (float) GUI_LAYOUT.getPointX(inputX);
            float ty = (float) GUI_LAYOUT.getPointY(inputY);
            float rotation = (System.currentTimeMillis() % 4000) / 4000f * 360f;
            guiGraphics.pose().pushPose();
            moveToAbsolute(guiGraphics, slot, tx, ty);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(0.75f, 0.75f, 0);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(rotation));
            guiGraphics.pose().translate(-16, -16, 0);
            guiGraphics.blit(PARTS_SLOT_TEXTURE, 0, 0, 0, 0, 32, 32, 32, 32);
            guiGraphics.pose().popPose();
            guiGraphics.pose().pushPose();
            guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(-rotation));
            guiGraphics.pose().translate(-16, -16, 0);
            guiGraphics.blit(PARTS_SLOT_TEXTURE, 0, 0, 0, 0, 32, 32, 32, 32);
            guiGraphics.pose().popPose();
            guiGraphics.pose().popPose();
            guiGraphics.pose().translate(-8, -8, 0);
            this.renderSlotBase(guiGraphics, slot);
            guiGraphics.pose().popPose();
        } else if (slot.index == 37) {
            float tx = (float) GUI_LAYOUT.getPointX(outputX);
            float ty = (float) GUI_LAYOUT.getPointY(outputY);
            float rotation = (System.currentTimeMillis() % 4000) / 4000f * 360f;
            guiGraphics.pose().pushPose();
            moveToAbsolute(guiGraphics, slot, tx, ty);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(0.75f, 0.75f, 0);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(rotation));
            guiGraphics.pose().translate(-16, -16, 0);
            guiGraphics.blit(PARTS_SLOT_TEXTURE, 0, 0, 0, 0, 32, 32, 32, 32);
            guiGraphics.pose().popPose();
            guiGraphics.pose().pushPose();
            guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(-rotation));
            guiGraphics.pose().translate(-16, -16, 0);
            guiGraphics.blit(PARTS_SLOT_TEXTURE, 0, 0, 0, 0, 32, 32, 32, 32);
            guiGraphics.pose().popPose();
            guiGraphics.pose().popPose();
            guiGraphics.pose().translate(-8, -8, 0);
            this.renderSlotBase(guiGraphics, slot);
            guiGraphics.pose().popPose();
        }
    }
    
    @Override
    protected void addRenderSlotHighlight(@NotNull GuiGraphics guiGraphics, @NotNull Slot slot, int mouseX, int mouseY, float partialTick) {
        if (slot.index == 36) {
            float tx = (float) GUI_LAYOUT.getPointX(inputX);
            float ty = (float) GUI_LAYOUT.getPointY(inputY);
            guiGraphics.pose().pushPose();
            moveToAbsolute(guiGraphics, slot, tx, ty);
            guiGraphics.pose().translate(-8, -8, 0);
            this.renderSlotHighlightBase(guiGraphics, slot, mouseX, mouseY, partialTick);
            guiGraphics.pose().popPose();
        } else if (slot.index == 37 ){
            float tx = (float) GUI_LAYOUT.getPointX(outputX);
            float ty = (float) GUI_LAYOUT.getPointY(outputY);
            guiGraphics.pose().pushPose();
            moveToAbsolute(guiGraphics, slot, tx, ty);
            guiGraphics.pose().translate(-8, -8, 0);
            this.renderSlotHighlightBase(guiGraphics, slot, mouseX, mouseY, partialTick);
            guiGraphics.pose().popPose();
        }
    }
    
    @Override
    protected boolean isHovering(@NotNull Slot slot, double mouseX, double mouseY) {
        
        if (slot.index == 36) {
            // 1. renderSlotで「スロット(0,0)」として定義した絶対座標を算出
            float baseX = GUI_LAYOUT.getPointX(inputX) -10;
            float baseY = GUI_LAYOUT.getPointY(inputY) -10;
            
            // 2. そのスロットの左上角の絶対座標
            float slotLeft = baseX + slot.x;
            float slotTop = baseY + slot.y;
            
            // 3. マウスが「スロットの左上」から「16px以内」にいるか判定
            return mouseX >= slotLeft && mouseX < (slotLeft + 20)
                    && mouseY >= slotTop  && mouseY < (slotTop + 20);
        } else if (slot.index == 37) {
            // 1. renderSlotで「スロット(0,0)」として定義した絶対座標を算出
            float baseX = GUI_LAYOUT.getPointX(outputX) -10;
            float baseY = GUI_LAYOUT.getPointY(outputY) -10;
            
            // 2. そのスロットの左上角の絶対座標
            float slotLeft = baseX + slot.x;
            float slotTop = baseY + slot.y;
            
            // 3. マウスが「スロットの左上」から「16px以内」にいるか判定
            return mouseX >= slotLeft && mouseX < (slotLeft + 20)
                    && mouseY >= slotTop  && mouseY < (slotTop + 20);
        }
        
        return super.isHovering(slot, mouseX, mouseY);
    }
}
