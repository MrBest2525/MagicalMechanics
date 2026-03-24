package com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.items;

import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.CoreInstance;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.FrameCore;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.core.FurnaceCoreInstance;
import net.minecraft.world.item.Item;

public class FurnaceCoreItem extends Item implements FrameCore {
    public FurnaceCoreItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public CoreInstance createInstance() {
        return new FurnaceCoreInstance();
    }
}
