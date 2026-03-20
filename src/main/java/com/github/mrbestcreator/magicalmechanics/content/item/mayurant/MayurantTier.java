package com.github.mrbestcreator.magicalmechanics.content.item.mayurant;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public interface MayurantTier extends Tier {
    String getId();
    
    int getMinMagicPower();
    
    int getMaxMagicPower();
    
    boolean getIsSmithingTable();
    
    Ingredient getBaseItem();
    
    Ingredient getAssemblyItem();
    
    Ingredient getCoreItem();
}
