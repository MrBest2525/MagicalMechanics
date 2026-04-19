package io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.items;

import io.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.FrameSide;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.SideInstance;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.side.energy.WirelessEnergySideTier;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.side.energy.WirelessMFInterfaceSideInstance;
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
