package io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.items;

import io.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.FrameSide;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.SideInstance;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.side.FurnaceSideInstance;
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
