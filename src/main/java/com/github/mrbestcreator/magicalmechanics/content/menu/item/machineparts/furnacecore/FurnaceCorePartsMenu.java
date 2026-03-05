package com.github.mrbestcreator.magicalmechanics.content.menu.item.machineparts.furnacecore;

import com.github.mrbestcreator.magicalmechanics.content.block.machine.frame.FrameBlockEntity;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.core.FurnaceCoreInstance;
import com.github.mrbestcreator.magicalmechanics.content.menu.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class FurnaceCorePartsMenu extends AbstractContainerMenu {
    
    public final FrameBlockEntity blockEntity;
    public final FurnaceCoreInstance furnaceCore;
    public final Level level;
    public final ContainerData data;
    
    public FurnaceCorePartsMenu(int containerId, Inventory playerInventory, IItemHandler dataInventory, ContainerData containerData, FrameBlockEntity blockEntity) {
        super(ModMenus.FURNACE_CORE_PARTS_MENU.get(), containerId);
        this.blockEntity = blockEntity;
        FurnaceCoreInstance furnaceCore = new FurnaceCoreInstance();
        if (blockEntity.coreInstance instanceof FurnaceCoreInstance furnaceCoreInstance) {
            furnaceCore = furnaceCoreInstance;
        }
        this.furnaceCore = furnaceCore;
        this.level = playerInventory.player.level();
        this.data = containerData;
        this.addDataSlots(containerData);
        
        // 1. プレイヤーインベントリ (Index 0 ~ 26) & ホットバー (Index 27 ~ 35)
        for (int i = 9; i <= 35; i++) {
            this.addSlot(new Slot(playerInventory, i, 0, 0));
        }
        for (int i = 0; i <= 8; i++) {
            this.addSlot(new Slot(playerInventory, i, 0, 0));
        }
        // 2. 燃料スロット (Index 36)
        this.addSlot(new FuelSlot(dataInventory, 0, 0, 0));

        // 3. 燃焼中アイテム同期用スロット (Index 37)
        // 画面外(x=-1000)に配置し、プレイヤーの操作を完全に禁止する
        this.addSlot(new SlotItemHandler(dataInventory, 1, -1000, -1000) {
            @Override public boolean mayPickup(@NotNull Player player) { return false; }
            @Override public boolean mayPlace(@NotNull ItemStack stack) { return false; }
        });

        // 3. 前回燃焼アイテム同期用スロット (Index 38)
        this.addSlot(new SlotItemHandler(dataInventory, 2, -1000, -1000) {
            @Override public boolean mayPickup(@NotNull Player player) { return false; }
            @Override public boolean mayPlace(@NotNull ItemStack stack) { return false; }
        });
        
    }
    
    // --- サーバー側 ---
    public FurnaceCorePartsMenu(int containerId, Inventory playerInventory, FrameBlockEntity blockEntity) {
        this(containerId, playerInventory, getInventory(blockEntity), blockEntity.data, blockEntity);
        
    }
    
    // --- クライアント側 ---
    public FurnaceCorePartsMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory, new ItemStackHandler(3), new SimpleContainerData(2), (FrameBlockEntity) playerInventory.player.level().getBlockEntity(buf.readBlockPos()));
    }
    
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyStack = sourceStack.copy();
        
        // 0番(マシン)からプレイヤーへ
        if (index >= 36) {
            if (!moveItemStackTo(sourceStack, 0, 36, true)) {
                return ItemStack.EMPTY;
            }
        }
        // プレイヤーから 0番(マシン)へ
        else {
            // 燃料かどうかチェックしてマシンへ送る試行
            if (sourceStack.getBurnTime(RecipeType.SMELTING) > 0) {
                // マシンの 36番スロット (Fuel) へ試行
                if (!moveItemStackTo(sourceStack, 36, 37, false)) {
                    // マシンがいっぱいなら、プレイヤーインベントリ内での移動に回す
                    if (!moveWithinPlayerInventory(sourceStack, index)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                // 燃料でないなら、プレイヤーインベントリ内（ホットバー ↔ メイン）で移動
                if (!moveWithinPlayerInventory(sourceStack, index)) {
                    return ItemStack.EMPTY;
                }
            }
        }
        
        if (sourceStack.isEmpty()) sourceSlot.set(ItemStack.EMPTY);
        else sourceSlot.setChanged();
        
        return copyStack;
    }
    
    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, blockEntity.getBlockState().getBlock());
    }
    
    // ヘルパーメソッド: プレイヤーのメインインベントリとホットバー間の移動
    private boolean moveWithinPlayerInventory(ItemStack stack, int index) {
        if (index < 27) { // メインインベントリ -> ホットバー
            return moveItemStackTo(stack, 27, 36, false);
        } else { // ホットバー -> メインインベントリ
            return moveItemStackTo(stack, 0, 27, false);
        }
    }
    
    private static ItemStackHandler getInventory(FrameBlockEntity be) {
        if (be.coreInstance instanceof FurnaceCoreInstance furnaceCore) {
            return furnaceCore.inventory;
        }
        // nullを返すと呼び出し先でエラーになる可能性があるため、空のハンドラーを返す
        return new ItemStackHandler(3);
    }
    
    private static class FuelSlot extends SlotItemHandler {
        public FuelSlot(IItemHandler itemHandler, int index, int x, int y) {
            super(itemHandler, index, x, y);
        }
        
        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            // 例燃焼時間のあるアイテムのみ許可
            return stack.getBurnTime(RecipeType.SMELTING) > 0;
            
        }
    }
}
