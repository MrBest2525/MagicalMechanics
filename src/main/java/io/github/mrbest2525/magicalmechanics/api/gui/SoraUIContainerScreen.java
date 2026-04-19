package io.github.mrbest2525.magicalmechanics.api.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

public abstract class SoraUIContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    protected final SoraUI soraUI;
    
    public SoraUIContainerScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        soraUI = new SoraUI();
        soraUI.load(loadFile());
        bindRenderer(soraUI);
        soraUI.bindComplete();
    }
    
    protected abstract ResourceLocation loadFile();
    
    protected abstract void bindRenderer(SoraUI soraUI);
    
    @Override
    protected void init() {
        super.init();
        soraUI.init(this.width, this.height);
    }
    
    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
    
    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        long window = Minecraft.getInstance().getWindow().getWindow();
        // 状態を汚さないよう念のため push/pop。
        guiGraphics.pose().pushPose();
        soraUI.draw(guiGraphics, partialTick, mouseX, mouseY, window, false);
        guiGraphics.pose().popPose();
    }
    
    @Override
    public void renderTransparentBackground(@NotNull GuiGraphics guiGraphics) {
        // 空にする。これで「画面が暗くなる」のが消えます（1.20.1以降のデフォ挙動の抑制）。
    }
    
    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // 空にする。これで「Inventory」やコンテナ名のテキスト描画が消えます。
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        long window = Minecraft.getInstance().getWindow().getWindow();
        soraUI.draw(null, 1.0f, (int) mouseX, (int) mouseY, window, true);
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
