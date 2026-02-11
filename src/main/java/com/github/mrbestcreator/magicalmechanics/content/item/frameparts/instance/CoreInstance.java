package com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public interface CoreInstance {
    void save(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider);
    void load(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider);
    void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity);
   
    default long getEnergy() {return 0;}
    default boolean addEnergy(long energy) {return false;}
    default boolean consumeEnergy(long energy) {return false;}
    
    default float getThermal() {return 0;}
    default boolean addThermal(float thermal) {return false;}
    default boolean consumeThermal(float thermal) {return false;}
    //    プレイヤーはMana
    //    機械はMagicFlux
    default long getMagicFlux() {return 0;}
    default boolean addMagicFlux(long magicFlux) {return false;}
    default boolean consumeMagicFlux(long magicFlux) {return false;}
}
