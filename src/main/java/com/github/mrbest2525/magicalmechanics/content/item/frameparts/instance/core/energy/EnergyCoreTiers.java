package com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.core.energy;

import com.github.mrbest2525.magicalmechanics.util.math.MMLong;

public enum EnergyCoreTiers implements EnergyCoreTier {
    UNLIMITED("unlimited", true, EnergyCoreTier.UNLIMITED, EnergyCoreTier.UNLIMITED, true, EnergyCoreTier.UNLIMITED, true, EnergyCoreTier.UNLIMITED);
    
    // --- TierName ---
    private final String tierName;
    // --- Energy ---
    private final boolean unlimitedEnergy;
    private final MMLong maxEnergy;
    private final MMLong defaultEnergy;
    // --- Input ---
    private final boolean unlimitedInput;
    private final MMLong maxInput;
    // --- Extract ---
    private final boolean unlimitedExtract;
    private final MMLong maxExtract;
    
    
    EnergyCoreTiers(String tierName, boolean unlimitedEnergy, MMLong maxEnergy, MMLong defaultEnergy, boolean unlimitedInput, MMLong maxInput, boolean unlimitedExtract, MMLong maxExtract) {
        this.tierName = tierName;
        this.unlimitedEnergy = unlimitedEnergy;
        this.maxEnergy = maxEnergy.copy();
        this.defaultEnergy = defaultEnergy.copy();
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
    public boolean getUnlimitedEnergy() {
        return unlimitedEnergy;
    }
    
    @Override
    public MMLong getMaxEnergy() {
        return maxEnergy;
    }
    
    @Override
    public MMLong getDefaultEnergy() {
        return defaultEnergy;
    }
    
    @Override
    public boolean getUnlimitedInput() {
        return unlimitedInput;
    }
    
    @Override
    public MMLong getMaxInput() {
        return maxInput;
    }
    
    @Override
    public boolean getUnlimitedExtract() {
        return unlimitedExtract;
    }
    
    @Override
    public MMLong getMaxExtract() {
        return maxExtract;
    }
}
