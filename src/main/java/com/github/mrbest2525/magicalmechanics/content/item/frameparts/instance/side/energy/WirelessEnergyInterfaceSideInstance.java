package com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.side.energy;

import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.SideInstance;
import com.github.mrbest2525.magicalmechanics.util.math.MMLong;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class WirelessEnergyInterfaceSideInstance implements SideInstance, IWirelessEnergyProvider {
    
    private final MachineFrameBlockEntity blockEntity;
    private final WirelessEnergySideTier tier;
    
    private final MMLong workLimit = new MMLong();
    
    public WirelessEnergyInterfaceSideInstance(MachineFrameBlockEntity blockEntity, WirelessEnergySideTier tier) {
        this.blockEntity = blockEntity;
        this.tier = tier;
    }
    
    @Override
    public void save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
    
    }
    
    @Override
    public void load(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
    
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
    public MMLong extractWirelessEnergy(MMLong maxExtract, boolean simulate) {
        if (blockEntity.coreInstance == null || !blockEntity.coreInstance.supportsEnergy()) {
            return new MMLong(0);
        }
        
        // 1. Side側の転送レート制限
        if (tier.getUnlimitedExtractEnergy()) {
            workLimit.set(maxExtract);
        } else {
            workLimit.set(MMLong.min(maxExtract, tier.getMaxExtractEnergy()));
        }
        
        // 2. Coreの「現在の蓄電量」による制限
        // 容量が無限でも、持っていない分は出せないので、常に残量と比較する
        workLimit.set(MMLong.min(workLimit, blockEntity.coreInstance.getEnergy()));
        
        // 3. 実行
        if (workLimit.atLeast(1L)) {
            if (simulate) return workLimit.copy();
            
            // 実際に消費を試みる（成功すればその分を返す）
            if (blockEntity.coreInstance.consumeEnergy(workLimit)) {
                return workLimit.copy();
            }
        }
        return new MMLong(0);
    }
    
    @Override
    public MMLong insertWirelessEnergy(MMLong maxInsert, boolean simulate) {
        if (blockEntity.coreInstance == null || !blockEntity.coreInstance.supportsEnergy()) {
            return new MMLong(0);
        }
        
        // 1. Side側の転送レート制限
        if (tier.getUnlimitedInputEnergy()) {
            workLimit.set(maxInsert);
        } else {
            workLimit.set(MMLong.min(maxInsert, tier.getMaxInputEnergy()));
        }
        
        // 2. 実行
        if (workLimit.atLeast(1L)) {
            if (simulate) return workLimit.copy();
            
            // Coreが「容量無限」なら、addEnergy内部の満タンチェックを無視して
            // 確実に受け入れられるはず。
            if (blockEntity.coreInstance.addEnergy(workLimit)) {
                return workLimit.copy();
            }
        }
        return new MMLong(0);
    }
}
