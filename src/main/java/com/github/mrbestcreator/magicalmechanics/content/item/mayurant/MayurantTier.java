package com.github.mrbestcreator.magicalmechanics.content.item.mayurant;

import net.minecraft.world.item.Tier;

public interface MayurantTier extends Tier {
    int getMinMagicPower();
    
    int getMaxMagicPower();
}
