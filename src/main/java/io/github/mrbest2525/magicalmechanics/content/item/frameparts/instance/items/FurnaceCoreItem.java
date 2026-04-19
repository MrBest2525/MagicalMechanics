package io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.items;

import io.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.CoreInstance;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.FrameCore;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.core.FurnaceCoreInstance;
import net.minecraft.world.item.Item;

public class FurnaceCoreItem extends Item implements FrameCore {
    public FurnaceCoreItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public CoreInstance createInstance(MachineFrameBlockEntity blockEntity) {
        return new FurnaceCoreInstance();
    }
}
