package com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance;

import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import com.github.mrbest2525.magicalmechanics.util.math.MMLong;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public interface CoreInstance {
    MMLong MMLONG_ZERO = new MMLong(0);
    
    void save(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider);
    void load(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider);
    boolean tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull MachineFrameBlockEntity blockEntity);
    
    void onAttached(@NotNull MachineFrameBlockEntity frame);
    void onDetached(@NotNull MachineFrameBlockEntity frame);
    
    default AbstractContainerMenu getMenu(int id, Inventory inventory, MachineFrameBlockEntity blockEntity) {return null;}
    
    /**
     * このコアがエネルギーを扱えるかどうか
     */
    default boolean supportsEnergy() {return false;
    }
    
    /**
     * エネルギーを取得
     */
    default MMLong getEnergy() {
        return MMLONG_ZERO;
    }
    
    /**
     * エネルギーを増やす
     */
    default boolean addEnergy(MMLong energy) {
        return false;
    }
    
    /**
     * getEnergyで消費
     */
    default boolean consumeEnergy(MMLong energy) {
        return false;}
    
    default boolean supportsThermal() {return false;}
    default float getThermal() {return 0;}
    default boolean addThermal(float thermal) {return false;}
    default boolean consumeThermal(float thermal) {return false;}
    default boolean setTargetThermal(float thermal) {return false;}
    //    プレイヤーはMana
    //    機械はMagicFlux
    default boolean supportsMagicFlux() {return false;}
    default long getMagicFlux() {return 0;}
    default boolean addMagicFlux(long magicFlux) {return false;}
    default boolean consumeMagicFlux(long magicFlux) {return false;}
}
