package com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.side.energy;

import com.github.mrbest2525.magicalmechanics.util.math.MMLong;

public enum WirelessEnergySideTiers implements WirelessEnergySideTier{
    UNLIMITED("unlimited", true, WirelessEnergySideTier.UNLIMITED, true, WirelessEnergySideTier.UNLIMITED);
    
    // --- TierName ---
    private final String tierName;
    // --- Input ---
    private final boolean unlimitedInput;
    private final MMLong maxInput;
    // --- Extract ---
    private final boolean unlimitedExtract;
    private final MMLong maxExtract;
    
    
    WirelessEnergySideTiers(String tierName, boolean unlimitedInput, MMLong maxInput, boolean unlimitedExtract, MMLong maxExtract) {
        this.tierName = tierName;
        this.unlimitedInput = unlimitedInput;
        this.maxInput = maxInput.copy();
        this.unlimitedExtract = unlimitedExtract;
        this.maxExtract = maxExtract.copy();
    }
    
    @Override
    public String getTierName() {
        return tierName;
    }
    
    @Override
    public boolean getUnlimitedInputEnergy() {
        return unlimitedInput;
    }
    
    @Override
    public MMLong getMaxInputEnergy() {
        return maxInput;
    }
    
    @Override
    public boolean getUnlimitedExtractEnergy() {
        return unlimitedExtract;
    }
    
    @Override
    public MMLong getMaxExtractEnergy() {
        return maxExtract;
    }
}
