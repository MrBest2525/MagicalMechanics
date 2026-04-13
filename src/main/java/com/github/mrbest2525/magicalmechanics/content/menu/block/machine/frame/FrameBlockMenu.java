package com.github.mrbest2525.magicalmechanics.content.menu.block.machine.frame;

import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import com.github.mrbest2525.magicalmechanics.content.menu.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class FrameBlockMenu extends AbstractContainerMenu {
    
    public final MachineFrameBlockEntity blockEntity;
    private final Level level;
    
    // --- サーバー側 ---
    public FrameBlockMenu(int containerId, Inventory playerInventory, MachineFrameBlockEntity blockEntity) {
        super(ModMenus.MACHINE_FRAME_MENU.get(), containerId);
        this.blockEntity = blockEntity;
        this.level = playerInventory.player.level();
    }
    
    // --- クライアント側 ---
    public FrameBlockMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId,
                playerInventory,
                (MachineFrameBlockEntity) playerInventory.player.level()
                        .getBlockEntity(buf.readBlockPos()));
    }
    
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {
        return ItemStack.EMPTY;
    }
    
    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
