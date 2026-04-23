package io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.settingpart;

import io.github.mrbest2525.magicalmechanics.MagicalMechanics;
import io.github.mrbest2525.magicalmechanics.api.SourceType;
import io.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.CoreInstance;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.SettingPartInstance;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.side.energy.WirelessMFInterfaceSideInstance;
import io.github.mrbest2525.magicalmechanics.util.math.MMLong;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class MFWirelessChargerSettingPartInstance implements SettingPartInstance {
    
    private BlockPos linkPos;
    
    // --- Zero-New 用のキャッシュフィールド ---
    private final MMLong transferBuffer = new MMLong();
    private final MMLong requestAmount = new MMLong(1000); // 転送レート。定数にするなら static final でも可
    private final MMLong acceptedBuffer = new MMLong();
    private final MMLong dummyBuffer = new MMLong(); // 戻り値を受け取るが使わない時用
    
    @Override
    public ResourceLocation getInstanceId() {
        return ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "mf_wireless_charger");
    }
    
    @Override
    public void save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        if (linkPos != null) {
            tag.put("LinkedPos", NbtUtils.writeBlockPos(linkPos));
        }
    }
    
    @Override
    public void load(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        if (tag.contains("LinkedPos")) {
            this.linkPos = NbtUtils.readBlockPos(tag, "LinkedPos").orElse(null);
        }
    }
    
    @Override
    public boolean tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull MachineFrameBlockEntity blockEntity) {
        if (linkPos == null) return false;
        if (!level.isLoaded(linkPos)) return false;
        BlockEntity sourceBE = level.getBlockEntity(linkPos);
        if (sourceBE == null) return false;
        if (!(sourceBE instanceof MachineFrameBlockEntity mfBE)) return false;
        if (mfBE.getSideInstance() == null) return false;
        if (!(mfBE.getSideInstance() instanceof WirelessMFInterfaceSideInstance mfInstance)) return false;
        CoreInstance core = blockEntity.getCoreInstance();
        if (core == null) return false;
        
        // --- エネルギー転送処理 (フィールドの MMLong を再利用) ---
        
        // 1. 相手からいくら出せるか確認 (simulate=true)
        mfInstance.extractWirelessEnergy(transferBuffer, requestAmount, true);
        if (transferBuffer.isZero()) return false;
        
        // 2. 自分がいくら受け取れるか確認 (simulate=true)
        core.addEnergy(acceptedBuffer, transferBuffer, true);
        if (acceptedBuffer.isZero()) return false;
        
        // 3. 確定した量(acceptedBuffer)を相手から実際に引く (simulate=false)
        // 引いた結果は transferBuffer に上書きされる
        mfInstance.extractWirelessEnergy(transferBuffer, acceptedBuffer, false);
        
        // 4. 実際に自分の Core に入れる (simulate=false)
        core.addEnergy(dummyBuffer, transferBuffer, false);
        
        blockEntity.setChanged();
        return true;
    }
    
    @Override
    public void onAttached(@NotNull MachineFrameBlockEntity frame) {
    
    }
    
    @Override
    public void onDetached(@NotNull MachineFrameBlockEntity frame) {
    
    }
    
    @Override
    public boolean canAcceptTarget(MachineFrameBlockEntity frame, SourceType type, BlockPos pos, Player player) {
        return type.equals(SourceType.MagicalFlux);
    }
    
    @Override
    public void onLinkEstablished(MachineFrameBlockEntity frame, SourceType type, BlockPos pos) {
        if (type.equals(SourceType.MagicalFlux)) {
            linkPos = pos;
            frame.setChanged();
        }
    }
}
