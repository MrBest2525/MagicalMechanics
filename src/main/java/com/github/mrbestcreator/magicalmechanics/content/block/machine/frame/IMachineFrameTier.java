package com.github.mrbestcreator.magicalmechanics.content.block.machine.frame;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;

public interface IMachineFrameTier {
    String getMachineFrameId();
    BlockBehaviour.Properties getBlockProperties();
    Item.Properties getItemProperties();
    TagKey<Item> getAssemblyItem();
    MachineFrameStat getMachineFrameStat();
    
    record MachineFrameStat(
            boolean isStable       // ネザライト等の「環境耐性」フラグ
    ) {}
}

