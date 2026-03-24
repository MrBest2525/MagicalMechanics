package com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.core.energy;

import com.github.mrbest2525.magicalmechanics.util.math.MMLong;

public interface EnergyCoreTier {
    MMLong UNLIMITED = new MMLong(0);
    
    // --- TierName ---
    String getTierName();
    
    // --- Energy ---
    boolean getUnlimitedEnergy();
    
    MMLong getMaxEnergy();
    
    MMLong getDefaultEnergy();
    
    // --- Input ---
    boolean getUnlimitedInput();
    
    MMLong getMaxInput();
    
    // --- Extract ---
    boolean getUnlimitedExtract();
    
    MMLong getMaxExtract();
}
