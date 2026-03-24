package com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.core.energy;

import com.github.mrbestcreator.magicalmechanics.util.math.MMLong;

public interface EnergyCoreTier {
    static final MMLong UNLIMITED = new MMLong(0);
    
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
