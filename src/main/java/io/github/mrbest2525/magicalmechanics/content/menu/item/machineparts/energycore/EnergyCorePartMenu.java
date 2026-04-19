package io.github.mrbest2525.magicalmechanics.content.menu.item.machineparts.energycore;

import io.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.CoreInstance;
import io.github.mrbest2525.magicalmechanics.content.menu.ModMenus;
import io.github.mrbest2525.magicalmechanics.content.menu.util.MMAbstractContainerMenu;
import io.github.mrbest2525.magicalmechanics.network.packet.menu.machineframe.MFCorePart.MFCorePartMenuSyncPayload;
import io.github.mrbest2525.magicalmechanics.util.math.MMLong;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class EnergyCorePartMenu extends MMAbstractContainerMenu<MachineFrameBlockEntity> {
    
    private final Inventory playerInventory;
    
    private MFCorePartMenuSyncPayload lastPayload;
    
    private final MMLong lastCurrentBuffer = new MMLong();
    private final MMLong lastMaxBuffer = new MMLong();
    
    private final MMLong tempBuffer = new MMLong();
    
    private boolean lastUnlimitedState = false;
    
    private boolean firstFlag = true;
    
    public EnergyCorePartMenu(@Nullable MenuType<?> menuType, int containerId, Inventory playerInventory, MachineFrameBlockEntity blockEntity) {
        super(menuType, containerId, playerInventory, blockEntity);
        this.playerInventory = playerInventory;
    }
    
    // A. クライアント側（登録処理のラムダから呼ばれる）
    public EnergyCorePartMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        // パケットから座標を読み取って、下の本尊コンストラクタへ投げる
        this(id, inv, getAndValidateBE(inv.player.level(), buf.readBlockPos(), MachineFrameBlockEntity.class));
    }
    
    // B. サーバー側（BEから直接呼ばれる） & Aからの転送先
    public EnergyCorePartMenu(int id, Inventory playerInv, MachineFrameBlockEntity be) {
        this(ModMenus.ENERGY_CORE_PART_MENU.get(), id, playerInv, be);
    }
    
    @Override
    protected void addContainerSlot() {
    
    }
    
    @Override
    protected int getContainerSize() {
        return 0;
    }
    
    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        
        if (this.blockEntity == null || blockEntity.getLevel() == null || blockEntity.getLevel().isClientSide) return;
        
        CoreInstance core = blockEntity.getCoreInstance();
        if (core == null || !core.supportsEnergy()) return;
        
        // 1. 現在の値を tempBuffer にコピー (getEnergy(tempBuffer) を使用)
        core.getEnergy(tempBuffer);
        boolean unlimited = core.supportsEnergyUnlimited();
        
        // 2. 「前回のバッファ」と比較する
        // MMLong に isEqualTo(MMLong other) があるので、それを利用
        if (firstFlag || !tempBuffer.isEqualTo(lastCurrentBuffer) || unlimited != lastUnlimitedState) {
            
            // 3. 変化があった場合のみパケット送出
            core.getMaxEnergy(lastMaxBuffer); // Maxも一応最新を取る
            MFCorePartMenuSyncPayload payload = createSyncPayload(tempBuffer, lastMaxBuffer, unlimited);
            
            if (playerInventory.player instanceof ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(serverPlayer, payload);
            }
            
            // 4. 次回比較のために「今回の値」を「前回のバッファ」に保存
            lastCurrentBuffer.set(tempBuffer);
            this.lastUnlimitedState = unlimited;
            
            firstFlag = false;
        }
    }
    
    /**
     * 送信用パケット(Record)を組み立てる
     */
    private MFCorePartMenuSyncPayload createSyncPayload(MMLong current, MMLong max, boolean unlimited) {// 1. モード決定: 0:正確(両方long), 1:指数(どちらかが巨大数), 2:無制限
        byte mode = unlimited ? (byte)2 : (current.isPromoted() || max.isPromoted() ? (byte)1 : (byte)0);
        
        // 2. 現在値(current)のパッキング
        long cRaw = 0;
        int cMant = 0;
        int cExp = 0;
        
        if (!current.isPromoted()) {
            cRaw = current.asLong();
        } else {
            // 巨大数の場合のみ、表示用の仮数と指数を取得
            // ※BigIntegerのtoStringは重いですが、パケットを送る「変化時のみ」なら許容範囲です
            String curStr = current.toString();
            cMant = parseMantissa(curStr, 4);
            cExp = curStr.length() - 1;
        }
        
        // 3. 最大値(max)のパッキング
        long mRaw = 0;
        int mMant = 0;
        int mExp = 0;
        
        if (!max.isPromoted()) {
            mRaw = max.asLong();
        } else {
            String maxStr = max.toString();
            mMant = parseMantissa(maxStr, 4);
            mExp = maxStr.length() - 1;
        }
        
        return new MFCorePartMenuSyncPayload(
                mode,
                cRaw,
                mRaw,
                cMant,
                cExp,
                mMant,
                mExp
        );
    }
    
    /**
     * 文字列から上位桁を抽出するユーティリティ
     */
    private int parseMantissa(String s, int precision) {
        if (s.equals("0")) return 0;
        int len = s.length();
        if (len <= precision) {
            return Integer.parseInt(s) * (int) Math.pow(10, precision - len);
        }
        return Integer.parseInt(s.substring(0, precision));
    }
    
    /**
     * ハンドラーから呼ばれ、クライアント側のMenuデータを更新する
     */
    public void updateFromPayload(MFCorePartMenuSyncPayload payload) {
        this.lastPayload = payload;
    }
    
    /**
     * Screen側で描画に使用する
     */
    public MFCorePartMenuSyncPayload getSyncData() {
        return lastPayload;
    }
    
}
