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
        
        this.addSlot(new FuelSlot(dataInventory, 0, 0, 0));
        
        // 2. プレイヤーインベントリ (Index 1 ~ 27)
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
//                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, j * 18, i * 18));
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 0, 0));
            }
        }
        
        // 3. ホットバー (Index 28 ~ 36)
        for (int i = 0; i < 9; ++i) {
//            this.addSlot(new Slot(playerInventory, i, i * 18, 18 * 3 + 2));
            this.addSlot(new Slot(playerInventory, i, 0, 0));
        }
        
    }
    
    // --- サーバー側 ---
    public FurnaceCorePartsMenu(int containerId, Inventory playerInventory, FrameBlockEntity blockEntity) {
        this(containerId, playerInventory, getInventory(blockEntity), blockEntity.data, blockEntity);
        
    }
    
    // --- クライアント側 ---
    public FurnaceCorePartsMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory, new ItemStackHandler(1), new SimpleContainerData(2), (FrameBlockEntity) playerInventory.player.level().getBlockEntity(buf.readBlockPos()));
    }
    
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyStack = sourceStack.copy();
        
        // 0番(マシン)からプレイヤーへ
        if (index < 1) {
            if (!moveItemStackTo(sourceStack, 1, 37, true)) {
                return ItemStack.EMPTY;
            }
        }
        // プレイヤーから 0番(マシン)へ
        else {
            if (!moveItemStackTo(sourceStack, 0, 1, false)) {
                return ItemStack.EMPTY;
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
    
    private static ItemStackHandler getInventory(FrameBlockEntity be) {
        if (be.coreInstance instanceof FurnaceCoreInstance furnaceCore) {
            return furnaceCore.inventory;
        }
        // nullを返すと呼び出し先でエラーになる可能性があるため、空のハンドラーを返す
        return new ItemStackHandler(0);
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
