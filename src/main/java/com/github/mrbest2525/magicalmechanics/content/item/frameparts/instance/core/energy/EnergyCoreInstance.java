package com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.core.energy;

import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.CoreInstance;
import com.github.mrbest2525.magicalmechanics.content.menu.item.machineparts.energycore.EnergyCorePartMenu;
import com.github.mrbest2525.magicalmechanics.util.math.MMLong;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class EnergyCoreInstance implements CoreInstance {
    private final EnergyCoreTier tier;
    private final MMLong energy;
    
    private final MMLong workBuffer = new MMLong();
    
    public EnergyCoreInstance(EnergyCoreTier tier) {
        this.tier = tier;
        this.energy = new MMLong(tier.getDefaultEnergy());
    }
    
    @Override
    public AbstractContainerMenu getMenu(int id, Inventory inventory, MachineFrameBlockEntity blockEntity) {
        return new EnergyCorePartMenu(id, inventory, blockEntity);
    }
    
    @Override
    public void save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        CompoundTag energyCore = new CompoundTag();
        
        energyCore.put("currentEnergy", this.energy.save());
        
        tag.put("EnergyCore", energyCore);
    }
    
    @Override
    public void load(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        CompoundTag energyCore = tag.getCompound("EnergyCore");
        
        energy.load(energyCore.getCompound("currentEnergy"));
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
    public boolean supportsEnergy() {
        return true;
    }
    
    @Override
    public boolean supportsEnergyUnlimited() {
        return tier.getUnlimitedEnergy();
    }
    
    @Override
    public MMLong getEnergy(MMLong buffer) {
        return buffer.set(energy);
    }
    
    @Override
    public MMLong addEnergy(MMLong resultBuffer, MMLong requestAmount, boolean simulate) {
        // 0. バリデーション
        if (requestAmount.isZero()) {
            resultBuffer.setZero();
            return resultBuffer;
        }
        
        // 1. 【受け取り制限】の判定
        if (tier.getUnlimitedInput()) {
            // 受け取り無制限なら、要求量をそのまま採用
            resultBuffer.set(requestAmount);
        } else {
            // 制限ありなら、要求量と Tier の上限のうち小さい方を採用
            MMLong.minTo(requestAmount, tier.getMaxInput(), resultBuffer);
        }
        
        // 2. 【最大量制限】の判定
        if (!tier.getUnlimitedEnergy()) {
            // 最大量に制限がある（有限）場合のみ、空き容量による絞り込みを行う
            // workBuffer = Max - Current
            workBuffer.set(tier.getMaxEnergy());
            workBuffer.sub(this.energy);
            
            // 現在の resultBuffer（受け入れようとしている量）を空き容量でクランプ
            MMLong.minTo(resultBuffer, workBuffer, resultBuffer);
        }
        // ※ getUnlimitedEnergy() が true なら、ここでの絞り込みをスルーして無限に溜まる
        
        // 3. 実行（シミュレーションでなければ内部数値に加算）
        if (!simulate && !resultBuffer.isZero()) {
            this.energy.add(resultBuffer);
        }
        return resultBuffer;
    }
    
    @Override
    public MMLong consumeEnergy(MMLong resultBuffer, MMLong requestAmount, boolean simulate) {
        // 0. バリデーション
        if (requestAmount.isZero()) {
            resultBuffer.setZero();
            return resultBuffer;
        }
        
        // 1. 【取り出し制限】の判定
        if (tier.getUnlimitedExtract()) {
            // 取り出し無制限なら、要求量をそのまま採用
            resultBuffer.set(requestAmount);
        } else {
            // 制限ありなら、要求量と Tier の上限のうち小さい方を採用
            MMLong.minTo(requestAmount, tier.getMaxExtract(), resultBuffer);
        }
        
        // 2. 【最大量（残量）制限】の判定
        if (!tier.getUnlimitedEnergy()) {
            // 最大量に制限がある（有限）場合、持っている分（this.energy）までしか出せない
            MMLong.minTo(resultBuffer, this.energy, resultBuffer);
        }
        // ※ getUnlimitedEnergy() が true なら、残量に関わらず「無限の魔力」として要求分を出す
        
        // 3. 実行（シミュレーションでなければ内部数値から減算）
        if (!simulate && !resultBuffer.isZero()) {
            // 負の数にならないようにガードして減算
            if (this.energy.atLeast(resultBuffer)) {
                this.energy.sub(resultBuffer);
            } else {
                // 残量以上に引き出した（Unlimitedモード時など）場合は 0 で止める
                this.energy.setZero();
            }
        }
        return resultBuffer;
    }
}
