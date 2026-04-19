package io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.side.energy;

import io.github.mrbest2525.magicalmechanics.util.math.MMLong;

public interface WirelessEnergySideTier {
    MMLong UNLIMITED = new MMLong(0);
    
    // --- TierName ---
    String getTierName();
    
    // --- Input ---
    boolean getUnlimitedInputEnergy();
    
    MMLong getMaxInputEnergy();
    
    // --- Extract ---
    boolean getUnlimitedExtractEnergy();
    
    MMLong getMaxExtractEnergy();
}
