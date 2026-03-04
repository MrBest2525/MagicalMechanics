package com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance;

import com.github.mrbestcreator.magicalmechanics.content.block.machine.frame.FrameBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public interface CoreInstance {
    void save(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider);
    void load(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider);
    boolean tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity);
    
    void onAttached(FrameBlockEntity frame);
    void onDetached(FrameBlockEntity frame);
    
    default AbstractContainerMenu getMenu(int id, Inventory inventory, FrameBlockEntity blockEntity) {return null;}
    
    default boolean supportsEnergy() {return false;}
    default long getEnergy() {return 0;}
    default boolean addEnergy(long energy) {return false;}
    default boolean consumeEnergy(long energy) {return false;}
    
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
