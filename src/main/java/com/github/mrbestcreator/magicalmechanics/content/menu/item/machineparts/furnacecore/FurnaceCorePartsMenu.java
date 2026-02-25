package com.github.mrbestcreator.magicalmechanics.content.menu.item.machineparts.furnacecore;

import com.github.mrbestcreator.magicalmechanics.content.block.machine.frame.FrameBlockEntity;
import com.github.mrbestcreator.magicalmechanics.content.menu.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class FurnaceCorePartsMenu extends AbstractContainerMenu {
    
    public final FrameBlockEntity blockEntity;
    public final Level level;
    public final ContainerData data;
    
    public FurnaceCorePartsMenu(int containerId, Inventory playerInventory, FrameBlockEntity blockEntity, ContainerData data) {
        super(ModMenus.FURNACE_CORE_PARTS_MENU.get(), containerId);
        this.blockEntity = blockEntity;
        this.level = playerInventory.player.level();
        this.data = data;
        this.addDataSlots(this.data);
    }
    
    // --- サーバー側 ---
    public FurnaceCorePartsMenu(int containerId, Inventory playerInventory, FrameBlockEntity blockEntity) {
        this(containerId, playerInventory, blockEntity, blockEntity.data);
    }
    
    // --- クライアント側 ---
    public FurnaceCorePartsMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId,
                playerInventory,
                (FrameBlockEntity) playerInventory.player.level()
                        .getBlockEntity(buf.readBlockPos()),
                new SimpleContainerData(2));
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
