package com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance;

import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public interface SideInstance {
    void save(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider);
    void load(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider);
    boolean tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull MachineFrameBlockEntity blockEntity);
    
    void onAttached(@NotNull MachineFrameBlockEntity frame);
    void onDetached(@NotNull MachineFrameBlockEntity frame);
    
    default AbstractContainerMenu getMenu(int id, Inventory inventory, MachineFrameBlockEntity blockEntity) {return null;}
}
