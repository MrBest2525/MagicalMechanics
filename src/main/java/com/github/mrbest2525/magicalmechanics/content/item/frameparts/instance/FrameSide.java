package com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance;

import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;

public interface FrameSide {
    SideInstance createInstance(MachineFrameBlockEntity blockEntity);
}
