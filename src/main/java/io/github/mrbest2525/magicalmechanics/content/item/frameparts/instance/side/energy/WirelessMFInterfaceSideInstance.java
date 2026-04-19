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
    public void extractWirelessEnergy(MMLong resultBuffer, MMLong maxExtract, boolean simulate) {
        // 引数の resultBuffer が null の場合に備えて、自分の resultBuffer をバックアップにする防御策
        MMLong targetBuffer = (resultBuffer != null) ? resultBuffer : this.resultBuffer;
        
        // 1. バリデーション
        if (blockEntity.getCoreInstance() == null || !blockEntity.getCoreInstance().supportsEnergy()) {
            targetBuffer.setZero();
            return;
        }
        
        // 2. サイド側の転送レート（Tier）による制限
        if (tier.getUnlimitedExtractEnergy()) {
            workLimit.set(maxExtract);
        } else {
            MMLong.minTo(maxExtract, tier.getMaxExtractEnergy(), workLimit);
        }
        
        // 3. CoreInstance 側に消費を依頼
        blockEntity.getCoreInstance().consumeEnergy(targetBuffer, workLimit, simulate);
    }
    
    @Override
    public void insertWirelessEnergy(MMLong resultBuffer, MMLong maxInsert, boolean simulate) {
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
}
