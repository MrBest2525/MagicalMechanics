package com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.core;

import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.CoreInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FurnaceCoreInstance implements CoreInstance {
    
    private float biomeThermal;
    private boolean isBiomeThermal = false;
    private float thermal = 25;
    
    @Override
    public void save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
    
    }
    
    @Override
    public void load(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
    
    }
    
    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (!isBiomeThermal) {
            biomeThermal = (level.getBiome(pos).value().getBaseTemperature() - 0.15f) * 30.77f;
            thermal = biomeThermal;
            isBiomeThermal = true;
        }
        
        float diff = biomeThermal - thermal;
        // 20tick/s 前提で数十秒スケール
        float k = 0.005f;
        // 差が小さいほどさらに遅くする
        thermal += diff * k * Math.min(1.0f, Math.abs(diff) / 30.0f);
    }
    
    @Override
    public float getThermal() {
        System.out.println("Thermal: "+ thermal);
        return thermal;
    }
}
