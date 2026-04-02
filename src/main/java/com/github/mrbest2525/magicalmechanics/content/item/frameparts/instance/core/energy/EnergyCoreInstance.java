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
    
    // TODO ここから下Energyの実装必須
    
    @Override
    public boolean supportsEnergy() {
        return true;
    }
    
    @Override
    public boolean supportsEnergyUnlimited() {
        return tier.getUnlimitedEnergy();
    }
    
    @Override
    public MMLong getEnergy() {
        return energy.copy();
    }
    
    @Override
    public boolean addEnergy(MMLong energy) {
        this.energy.add(energy);
        return true;
    }
    
    @Override
    public boolean consumeEnergy(MMLong energy) {
        // 1. まず「足りるか」だけをチェック
        if (this.energy.atLeast(energy)) {
            // 2. 足りる場合のみ、実際に減算して true を返す
            this.energy.sub(energy);
            return true;
        }
        // 3. 足りなければ何もしない（falseを返すだけ）
        return false;
    }
}
