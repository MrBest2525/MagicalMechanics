package com.github.mrbest2525.magicalmechanics.content.menu.block.machine.frame;

import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import com.github.mrbest2525.magicalmechanics.content.menu.ModMenus;
import com.github.mrbest2525.magicalmechanics.content.menu.util.MMAbstractContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public class FrameBlockSettingPartsMenu extends MMAbstractContainerMenu<MachineFrameBlockEntity> {
    
    public FrameBlockSettingPartsMenu(@Nullable MenuType<?> menuType, int containerId, Inventory playerInventory, MachineFrameBlockEntity blockEntity) {
        super(menuType, containerId, playerInventory, blockEntity);
    }
    
    // A. クライアント側（登録処理のラムダから呼ばれる）
    public FrameBlockSettingPartsMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        // パケットから座標を読み取って、下の本尊コンストラクタへ投げる
        this(id, inv, getAndValidateBE(inv.player.level(), buf.readBlockPos(), MachineFrameBlockEntity.class));
    }
    
    // B. サーバー側（BEから直接呼ばれる） & Aからの転送先
    public FrameBlockSettingPartsMenu(int id, Inventory playerInv, MachineFrameBlockEntity be) {
        super(ModMenus.MACHINE_FRAME_SETTING_PARTS_MENU.get(), id, playerInv, be);
    }
    
    @Override
    protected void addContainerSlot() {
    
    }
    
    @Override
    protected int getContainerSize() {
        return 1;
    }
}