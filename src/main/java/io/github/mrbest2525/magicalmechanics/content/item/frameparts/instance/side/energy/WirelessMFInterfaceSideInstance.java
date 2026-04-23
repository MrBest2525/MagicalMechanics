package io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.side.energy;

import io.github.mrbest2525.magicalmechanics.api.SourceType;
import io.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.SideInstance;
import io.github.mrbest2525.magicalmechanics.util.math.MMLong;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class WirelessMFInterfaceSideInstance implements SideInstance, IWirelessMFProvider {
    
    private final MachineFrameBlockEntity blockEntity;
    private final WirelessEnergySideTier tier;
    
    private final MMLong workLimit = new MMLong();
    
    private final MMLong resultBuffer = new MMLong();
    
    private BlockPos linkedSourcePos = null;
    private SourceType linkedType = SourceType.MagicalFlux;
    
    public WirelessMFInterfaceSideInstance(MachineFrameBlockEntity blockEntity, WirelessEnergySideTier tier) {
        this.blockEntity = blockEntity;
        this.tier = tier;
    }
    
    @Override
    public void save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        if (linkedSourcePos != null) {
            tag.putLong("LinkedPos", linkedSourcePos.asLong());
        }
    }
    
    @Override
    public void load(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        if (tag.contains("LinkedPos")) {
            this.linkedSourcePos = BlockPos.of(tag.getLong("LinkedPos"));
        }
    }
    
    @Override
    public boolean tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull MachineFrameBlockEntity blockEntity) {
        return false;
    }
    
    @Override
    public void onAttached(@NotNull MachineFrameBlockEntity frame) {
    
    }
    
    @Override
    public void onDetached(@NotNull MachineFrameBlockEntity frame) {
    
    }
    
    @Override
    public void extractWirelessEnergy(@NotNull MMLong resultBuffer, @NotNull MMLong maxExtract, boolean simulate) {
        
        // 1. バリデーション
        if (blockEntity.getCoreInstance() == null || !blockEntity.getCoreInstance().supportsEnergy()) {
            resultBuffer.setZero();
            return;
        }
        
        // 2. サイド側の転送レート（Tier）による制限
        if (tier.getUnlimitedExtractEnergy()) {
            workLimit.set(maxExtract);
        } else {
            MMLong.minTo(maxExtract, tier.getMaxExtractEnergy(), workLimit);
        }
        
        // 3. CoreInstance 側に消費を依頼
        blockEntity.getCoreInstance().consumeEnergy(resultBuffer, workLimit, simulate);
    }
    
    @Override
    public void insertWirelessEnergy(@NotNull MMLong resultBuffer, @NotNull MMLong maxInsert, boolean simulate) {
        // 1. バリデーション
        if (blockEntity.getCoreInstance() == null || !blockEntity.getCoreInstance().supportsEnergy()) {
            resultBuffer.setZero();
            return;
        }
        
        // 2. サイド側の転送レート（Tier）による制限
        if (tier.getUnlimitedInputEnergy()) {
            workLimit.set(maxInsert);
        } else {
            // maxInsert と Tier の上限のうち、小さい方を workLimit に入れる
            MMLong.minTo(maxInsert, tier.getMaxInputEnergy(), workLimit);
        }
        
        // 3. CoreInstance 側に注入を依頼
        // 空き容量のチェック、simulate 判定、実際の加算がすべて完結
        blockEntity.getCoreInstance().addEnergy(resultBuffer, workLimit, simulate);
    }
    
    @Override
    public void getAvailableWirelessEnergy(@NotNull MMLong resultBuffer) {
        // 1. Coreがないなら 0
        if (blockEntity.getCoreInstance() == null) {
            resultBuffer.setZero();
            return;
        }
        
        // 2. Coreの「生の残量」を取得
        blockEntity.getCoreInstance().getEnergy(resultBuffer);
        
        // 3. もし Tier 制限があるなら、その上限で丸める（ここが重要！）
        if (!tier.getUnlimitedExtractEnergy()) {
            MMLong.minTo(resultBuffer, tier.getMaxExtractEnergy(), resultBuffer);
        }
    }
    
    @Override
    public void getWirelessMaxEnergyCapacity(@NotNull MMLong resultBuffer) {
        if (blockEntity.getCoreInstance() == null) {
            resultBuffer.setZero();
            return;
        }
        // 基本は Core の最大容量を入れる
        blockEntity.getCoreInstance().getMaxEnergy(resultBuffer);
        
        // ただし、Tier による転送制限があるなら、それを「最大値」として上書き（制限）する
        if (!tier.getUnlimitedExtractEnergy()) {
            MMLong.minTo(resultBuffer, tier.getMaxExtractEnergy(), resultBuffer);
        }
    }
}
