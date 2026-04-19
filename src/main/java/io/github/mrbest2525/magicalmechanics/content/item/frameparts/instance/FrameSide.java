package io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance;

import io.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;

public interface FrameSide {
    SideInstance createInstance(MachineFrameBlockEntity blockEntity);
}
