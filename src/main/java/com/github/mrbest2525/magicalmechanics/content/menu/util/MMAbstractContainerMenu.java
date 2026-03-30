package com.github.mrbest2525.magicalmechanics.content.menu.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public abstract class MMAbstractContainerMenu<T extends BlockEntity> extends AbstractContainerMenu {
    protected T blockEntity;
    protected MMAbstractContainerMenu(MenuType<?> type, int id, Inventory playerInventory, T blockEntity) {
        super(type, id);
        this.blockEntity = blockEntity;
        // 1. プレイヤーインベントリを共通で配置
        addPlayerInventory(playerInventory);
        addContainerSlot();
    }
    
    // プレイヤーのインベントリ（36枠）を並べるメソッド
    private void addPlayerInventory(Inventory playerInventory) {
        // 1. メインインベントリ (3段 × 9列)
        // スロットインデックス 9 ～ 35
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = col * 18;
                int y = row * 18;
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, x, y));
            }
        }
        
        // 2. ホットバー (1段 × 9列)
        // スロットインデックス 0 ～ 8
        // メインインベントリから少し隙間(4px)を空けて配置するのがバニラ流
        int hotbarY = (3 * 18) + 4;
        for (int col = 0; col < 9; ++col) {
            int x = col * 18;
            this.addSlot(new Slot(playerInventory, col, x, hotbarY));
        }
    }
    
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        
        if (slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemstack = itemStack1.copy();
            
            // 1. もしクリックされたのが「プレイヤーのインベントリ(0-35)」なら
            if (index < 36) {
                // 機械側のスロット（36番以降）に移動を試みる
                if (!this.moveItemStackTo(itemStack1, 36, 36 + getContainerSize(), false)) {
                    return ItemStack.EMPTY;
                }
            }
            // 2. もしクリックされたのが「機械のスロット(36以降)」なら
            else {
                // プレイヤーのインベントリ（0-35）に移動を試みる
                if (!this.moveItemStackTo(itemStack1, 0, 36, true)) {
                    return ItemStack.EMPTY;
                }
            }
            
            if (itemStack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            
            if (itemStack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            
            slot.onTake(player, itemStack1);
        }
        
        return itemstack;
    }
    
    @Override
    public boolean stillValid(@NotNull Player player) {
        // blockEntityが有効か、かつプレイヤーとの距離が離れすぎていないか
        return this.blockEntity != null &&
                !this.blockEntity.isRemoved() &&
                player.distanceToSqr(this.blockEntity.getBlockPos().getCenter()) <= 64.0D;
    }
    
    /**
     * この中にaddSlot()を書く
     */
    protected  abstract void addContainerSlot();
    
    // 子クラスで「自分のスロット」を追加するためのフック
    protected abstract int getContainerSize();
    
    protected static <E extends BlockEntity> E getAndValidateBE(Level level, BlockPos pos, Class<E> clazz) {
        BlockEntity be = level.getBlockEntity(pos);
        if (clazz.isInstance(be)) return clazz.cast(be);
        throw new IllegalStateException(clazz.getSimpleName() + " not found at " + pos);
    }
}
