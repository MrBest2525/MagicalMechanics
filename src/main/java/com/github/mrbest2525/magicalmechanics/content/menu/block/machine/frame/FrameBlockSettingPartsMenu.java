package com.github.mrbest2525.magicalmechanics.content.menu.block.machine.frame;

import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.SettingPartsManager;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.SettingParts;
import com.github.mrbest2525.magicalmechanics.content.menu.ModMenus;
import com.github.mrbest2525.magicalmechanics.content.menu.util.MMAbstractContainerMenu;
import com.github.mrbest2525.magicalmechanics.network.packet.menu.machineframe.settingpert.DeselectPartPayload;
import com.github.mrbest2525.magicalmechanics.network.packet.menu.machineframe.settingpert.SelectPartPayload;
import com.github.mrbest2525.magicalmechanics.network.packet.menu.machineframe.settingpert.SyncFramePartsPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public class FrameBlockSettingPartsMenu extends MMAbstractContainerMenu<MachineFrameBlockEntity> {
    
    private UUID selectedPartUuid = null;
    
    private Inventory playerInventory;
    private Player player;
    
    public FrameBlockSettingPartsMenu(@Nullable MenuType<?> menuType, int containerId, Inventory playerInventory, MachineFrameBlockEntity blockEntity) {
        super(menuType, containerId, playerInventory, blockEntity);
        this.playerInventory = playerInventory;
        this.player = playerInventory.player;
    }
    
    // A. クライアント側（登録処理のラムダから呼ばれる）
    public FrameBlockSettingPartsMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        // パケットから座標を読み取って、下の本尊コンストラクタへ投げる
        this(id, inv, getAndValidateBE(inv.player.level(), buf.readBlockPos(), MachineFrameBlockEntity.class));
    }
    
    // B. サーバー側（BEから直接呼ばれる） & Aからの転送先
    public FrameBlockSettingPartsMenu(int id, Inventory playerInv, MachineFrameBlockEntity be) {
        this(ModMenus.MACHINE_FRAME_SETTING_PARTS_MENU.get(), id, playerInv, be);
    }
    
    @Override
    protected void addContainerSlot() {
        // 2. 仮想 Settingスロット (Index 36(input)-37(output))
        // ここで渡す ItemStackHandler は BE側のマネージャーと通信するための「窓口」になります
        addSlot(new SettingSlotInput(this, 0, 0, 0));
        addSlot(new SettingSlotOutput(this, 0, 0, 0));
    }
    
    @Override
    protected int getContainerSize() {
        // 追加するスロットが二つだから2
        return 2;
    }
    
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            
            // --- 出力スロット (SettingSlotOutput) からの取り出し処理 ---
            if (slot instanceof SettingSlotOutput outputSlot) {
                UUID uuid = this.getSelectedPartUuid();
                if (uuid == null) return ItemStack.EMPTY;
                
                // 1. マネージャーから実際に引き抜く（最大スタック数分）
                int toExtract = Math.min(itemstack1.getCount(), itemstack1.getMaxStackSize());
                int extracted = blockEntity.getSettingPartsManager().extractPartCount(uuid, toExtract);
                
                if (extracted <= 0) return ItemStack.EMPTY;
                
                // 2. 引き抜いた分をプレイヤーインベントリへ移動
                ItemStack toMove = itemstack.copyWithCount(extracted);
                if (!this.moveItemStackTo(toMove, 0, 36, true)) {
                    // インベントリがいっぱいならマネージャーに戻す（キャンセル処理）
                    blockEntity.getSettingPartsManager().addPart(toMove);
                    return ItemStack.EMPTY;
                }
                
                // 成功した場合は、スロットの更新を通知
                slot.onQuickCraft(itemstack1, toMove);
                return toMove;
            }
            
            // --- プレイヤーインベントリから入力スロット (SettingSlotInput) への移動 ---
            if (index < 36) {
                // SettingPartsなら入力スロットへ (addContainerSlotで追加した最初のスロットがInputと仮定)
                if (itemstack1.getItem() instanceof SettingParts) {
                    if (!this.moveItemStackTo(itemstack1, 36, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    return ItemStack.EMPTY;
                }
            }
            
            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        
        return itemstack;
    }
    
    public void setSelectedPart(@Nullable UUID uuid) {
        this.selectedPartUuid = uuid;
        
        // 1. プレイヤーが ServerPlayer かどうかで、論理サーバー側であることを判定
        if (this.player instanceof ServerPlayer serverPlayer) {
            // 2. 解除 (null) か 選択 (UUID) かで送るパケットを分ける
            if (uuid == null) {
                PacketDistributor.sendToPlayer(serverPlayer, new DeselectPartPayload(this.blockEntity.getBlockPos()));
            } else {
                PacketDistributor.sendToPlayer(serverPlayer, new SelectPartPayload(this.blockEntity.getBlockPos(), uuid));
            }
        }
    }
    
    public @Nullable UUID getSelectedPartUuid() {
        return selectedPartUuid;
    }
    
    
    
    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        
        // サーバー側の処理であることを確認
        if (this.player instanceof ServerPlayer serverPlayer) {
            // 現在のマネージャーの全データを取得してパケットを作成
            Map<UUID, SettingPartsManager.PartItemStack> currentParts =
                    this.blockEntity.getSettingPartsManager().getUUIDtoPartsItemStackMap();
            
            // クライアントへ送信
            serverPlayer.connection.send(new SyncFramePartsPayload(
                    this.blockEntity.getBlockPos(),
                    currentParts
            ));
        }
    }
    
    
    private static class  SettingSlotInput extends Slot {
        private final FrameBlockSettingPartsMenu menu;
        
        public SettingSlotInput(FrameBlockSettingPartsMenu menu, int index, int x, int y) {
            super(new SimpleContainer(1), index, x, y);
            this.menu = menu;
        }
        
        private SettingPartsManager getManager() {
            return this.menu.blockEntity.getSettingPartsManager();
        }
        
        @Override
        public boolean mayPlace(ItemStack stack) {
            // 1. SettingParts（または対応するインターフェース/クラス）を継承しているか
            if (!(stack.getItem() instanceof SettingParts)) {
                return false;
            }
            
            // 2. 現在選択されているパーツと一致するか
            UUID currentUuid = menu.getSelectedPartUuid();
            if (currentUuid == null) return true; // 選択されてなければ何も入らない
            
            SettingPartsManager.PartItemStack target = getManager().getPartItemStack(currentUuid);
            
            // 3. 種類とデータが一致しているか（個数無視の判定）
            return ItemStack.isSameItemSameComponents(target.getItemStack(), stack);
        }
        
        @Override
        public boolean mayPickup(@NotNull Player player) {
            return false;
        }
        
        @Override
        public void set(@NotNull ItemStack stack) {
            if (!stack.isEmpty()) {
                UUID currentUuid = menu.getSelectedPartUuid();
                // マネージャーに個数を加算（addCountなどの自前メソッド）
                menu.setSelectedPart(getManager().addPart(stack));
                
                // 重要：吸い取ったのでスタックを空にする
                stack.setCount(0);
            }
            // スロット自体は常に空に見えるようにする（または一瞬置いて吸い込まれる演出）
            super.set(ItemStack.EMPTY);
        }
    }
    
    private static class SettingSlotOutput extends Slot {
        private final FrameBlockSettingPartsMenu menu;
        
        public SettingSlotOutput(FrameBlockSettingPartsMenu menu, int index, int x, int y) {
            super(new SimpleContainer(1), index, x, y);
            this.menu = menu;
        }
        
        private SettingPartsManager getManager() {
            return this.menu.blockEntity.getSettingPartsManager();
        }
        
        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return false;
        }
        
        @Override
        public boolean mayPickup(@NotNull Player player) {
            return true;
        }
        
        @Override
        public @NotNull ItemStack getItem() {
            UUID uuid = menu.getSelectedPartUuid();
            if (uuid == null) return ItemStack.EMPTY;
            
            SettingPartsManager.PartItemStack target = getManager().getPartItemStack(uuid);
            if (target.isEmpty() || target.getCount() <= 0) return ItemStack.EMPTY;
            
            // 在庫があるなら、最大64個分を「見本」として表示
            return target.getItemStack().copyWithCount(Math.min(target.getCount(), getMaxStackSize(target.getItemStack())));
        }
        
        @Override
        public @NotNull ItemStack remove(int amount) {
            UUID uuid = menu.getSelectedPartUuid();
            if (uuid == null) return ItemStack.EMPTY;
            
            // 1. マネージャーに「数字」として引き抜きを依頼
            int extractedAmount = getManager().extractPartCount(uuid, amount);
            
            if (extractedAmount <= 0) return ItemStack.EMPTY;
            
            // 2. 引き抜けた数だけ、新しい ItemStack を作って「窓口」から出す
            // getPartItemStack().getItemStack() は「型紙（count 1）」を返す想定
            ItemStack result = getManager().getPartItemStack(uuid).getItemStack().copyWithCount(extractedAmount);
            
            this.setChanged();
            return result;
        }
    }
}