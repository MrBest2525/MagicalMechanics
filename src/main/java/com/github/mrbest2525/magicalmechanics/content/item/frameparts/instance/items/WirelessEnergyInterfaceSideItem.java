package com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.items;

import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.FrameSide;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.SideInstance;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.side.energy.WirelessEnergySideTier;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.side.energy.WirelessMFInterfaceSideInstance;
import net.minecraft.world.item.Item;

public class WirelessEnergyInterfaceSideItem extends Item implements FrameSide {
    
    private final WirelessEnergySideTier tier;
    
    public WirelessEnergyInterfaceSideItem(Item.Properties properties, WirelessEnergySideTier tier) {
        super(properties);
        this.tier = tier;
    }
    
    @Override
    public SideInstance createInstance(MachineFrameBlockEntity blockEntity) {
        return new WirelessMFInterfaceSideInstance(blockEntity, tier);
    }
}
