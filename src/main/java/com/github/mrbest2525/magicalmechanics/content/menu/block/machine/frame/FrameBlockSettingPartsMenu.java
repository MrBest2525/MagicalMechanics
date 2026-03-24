package com.github.mrbest2525.magicalmechanics.content.menu.block.machine.frame;

import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.SettingParts;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.core.FurnaceCoreInstance;
import com.github.mrbest2525.magicalmechanics.content.menu.ModMenus;
import com.github.mrbest2525.magicalmechanics.content.menu.util.PlayerInventoryUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FrameBlockSettingPartsMenu extends AbstractContainerMenu {
    
    public final MachineFrameBlockEntity blockEntity;
    public final Level level;
    
    private UUID selectedPartUuid = null;
    
    protected FrameBlockSettingPartsMenu(int containerId, Inventory playerInventory, IItemHandler dataInventory, ContainerData containerData, MachineFrameBlockEntity blockEntity) {
        super(ModMenus.MACHINE_FRAME_SETTING_PARTS_MENU.get(), containerId);
        this.blockEntity = blockEntity;
        this.level = playerInventory.player.level();
        
        // 1. プレイヤーインベントリ配置
        PlayerInventoryUtil.setPlayerInventorySlot(this, playerInventory);
        
        // 2. 仮想 Settingスロット (Index 36)
        // ここで渡す ItemStackHandler は BE側のマネージャーと通信するための「窓口」になります
        this.addSlot(new SettingSlot(this, 0, 0, 0)); // 座標はイメージ図に合わせて調整してください
    }
    
    // --- サーバー側 ---
    public FrameBlockSettingPartsMenu(int containerId, Inventory playerInventory, MachineFrameBlockEntity blockEntity) {
        this(containerId, playerInventory, getInventory(blockEntity), blockEntity.data, blockEntity);
        
    }
    
    // --- クライアント側 ---
    public FrameBlockSettingPartsMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory, new ItemStackHandler(1), new SimpleContainerData(2), (MachineFrameBlockEntity) playerInventory.player.level().getBlockEntity(buf.readBlockPos()));
    }
    
    // --- iframe 用のロジック ---
    
    private static ItemStackHandler getInventory(MachineFrameBlockEntity be) {
        if (be.coreInstance instanceof FurnaceCoreInstance furnaceCore) {
            return furnaceCore.inventory;
        }
        // nullを返すと呼び出し先でエラーになる可能性があるため、空のハンドラーを返す
        return new ItemStackHandler(3);
    }
    
    public void setSelectedPart(@Nullable UUID uuid) {
        this.selectedPartUuid = uuid;
    }
    
    public @Nullable UUID getSelectedPartUuid() {
        return selectedPartUuid;
    }
    
    /**
     * 仮想スロット（Index 36）にアイテムが置かれた、または取られた時の処理
     * ここで SettingPartsManager の addPart / removePart を叩く
     */
    public void handleSlotChanged(ItemStack stack) {
        if (selectedPartUuid == null) {
            // 新しくパーツを追加する場合
            if (!stack.isEmpty() && stack.getItem() instanceof SettingParts) {
                // サーバー側で addPart を実行（UUIDは新規発行）
                // 実際にはパケット経由、あるいはサーバー側 Menu の Slot.set で行う
            }
        }
    }
    
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;
        
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyStack = sourceStack.copy();
        
        // 36番以降からプレイヤーインベントリへ
        if (index >= 36) {
            if (!moveItemStackTo(sourceStack, 0, 36, true)) {
                return ItemStack.EMPTY;
            }
        }
        // プレイヤーインベントリからSettingSlotへ
        else {
            // SettingPartsかどうか
            if (sourceStack.getItem() instanceof SettingParts) {
                // マシンの 36番スロット (SettingSlot) へ試行
                if (!moveItemStackTo(sourceStack, 36, 37, false)) {
                    // マシンがいっぱいなら、プレイヤーインベントリ内での移動に回す
                    if (!PlayerInventoryUtil.moveWithinPlayerInventory(this, sourceStack, index)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                // SettingPartsでないなら、プレイヤーインベントリ内（ホットバー ↔ メイン）で移動
                if (!PlayerInventoryUtil.moveWithinPlayerInventory(this, sourceStack, index)) {
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
    
    /**
     * SettingPartsManager と直接やり取りするための特殊スロット
     */
    private class SettingSlot extends Slot {
        private final FrameBlockSettingPartsMenu menu;
        
        public SettingSlot(FrameBlockSettingPartsMenu menu, int index, int x, int y) {
            // ダミーの Container を渡すが、実際の操作はオーバーライドする
            super(new SimpleContainer(1), index, x, y);
            this.menu = menu;
        }
        
        @Override
        public @NotNull ItemStack getItem() {
            UUID uuid = menu.getSelectedPartUuid();
            if (uuid == null) return ItemStack.EMPTY;
            return menu.blockEntity.getSettingPartsManager().getItemStack(uuid);
        }
        
        @Override
        public void set(@NotNull ItemStack stack) {
            UUID uuid = menu.getSelectedPartUuid();
            if (stack.isEmpty()) {
                if (uuid != null) {
                    menu.blockEntity.getSettingPartsManager().removePart(uuid);
                    menu.setSelectedPart(null);
                }
            } else if (stack.getItem() instanceof SettingParts) {
                // 新しく追加
                menu.blockEntity.getSettingPartsManager().addPart(stack);
                // ※ ここで addPart した直後の UUID を選択状態にするロジックが必要
            }
            this.setChanged();
        }
        
        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return stack.getItem() instanceof SettingParts;
        }
    }
}
