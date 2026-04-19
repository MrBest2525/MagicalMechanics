package io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.items;

import io.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.CoreInstance;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.FrameCore;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.core.energy.EnergyCoreInstance;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.core.energy.EnergyCoreTier;
import net.minecraft.world.item.Item;

public class EnergyCoreItem extends Item implements FrameCore {
    private final EnergyCoreTier tier;
    
    public EnergyCoreItem(Properties properties, EnergyCoreTier tier) {
        super(properties);
        this.tier = tier;
    }
    
    @Override
    public CoreInstance createInstance(MachineFrameBlockEntity blockEntity) {
        return new EnergyCoreInstance(tier);
    }
    
    public EnergyCoreTier getTier() {
        return tier;
    }
}
