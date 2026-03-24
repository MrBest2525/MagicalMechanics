package com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.items;

import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.CoreInstance;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.FrameCore;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.core.energy.EnergyCoreInstance;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.core.energy.EnergyCoreTier;
import net.minecraft.world.item.Item;

public class EnergyCoreItem extends Item implements FrameCore {
    private final EnergyCoreTier tier;
    
    public EnergyCoreItem(Properties properties, EnergyCoreTier tier) {
        super(properties);
        this.tier = tier;
    }
    
    @Override
    public CoreInstance createInstance() {
        return new EnergyCoreInstance(tier);
    }
}
