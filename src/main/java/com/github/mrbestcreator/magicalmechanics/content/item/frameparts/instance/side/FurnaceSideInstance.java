package com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.side;

import com.github.mrbestcreator.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.SideInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FurnaceSideInstance implements SideInstance {
    // TODO ロジックの作成
    @Override
    public void save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
    
    }
    
    @Override
    public void load(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
    
    }
    
    @Override
    public boolean tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        return false;
    }
    
    @Override
    public void onAttached(MachineFrameBlockEntity frame) {
    
    }
    
    @Override
    public void onDetached(MachineFrameBlockEntity frame) {
    
    }
}
