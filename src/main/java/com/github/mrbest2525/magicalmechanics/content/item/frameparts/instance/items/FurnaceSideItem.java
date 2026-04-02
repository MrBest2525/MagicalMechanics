package com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.items;

import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.FrameSide;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.SideInstance;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.side.FurnaceSideInstance;
import net.minecraft.world.item.Item;

public class FurnaceSideItem extends Item implements FrameSide {
    public FurnaceSideItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public SideInstance createInstance(MachineFrameBlockEntity blockEntity) {
        return new FurnaceSideInstance();
    }
}
