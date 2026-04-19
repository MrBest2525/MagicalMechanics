package io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.core.energy;

import io.github.mrbest2525.magicalmechanics.util.math.MMLong;

public enum EnergyCoreTiers implements EnergyCoreTier {
    UNLIMITED("unlimited", true, EnergyCoreTier.UNLIMITED, EnergyCoreTier.UNLIMITED, true, EnergyCoreTier.UNLIMITED, true, EnergyCoreTier.UNLIMITED, 0xFF00AEEF),
    NORMAL("normal", false, new MMLong(1_000_000_000), new MMLong(0), false, new MMLong(1_000_000), true, new MMLong(1_000_000), 0xFF00FF00),
    ADVANCED("advanced", false, new MMLong(1_000_000_000), new MMLong(0), false, new MMLong(1_000_000), true, new MMLong(1_000_000), 0xFFFF00FF);
    
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
    // --- TextureColor ---
    private final int textureColor;
    
    
    EnergyCoreTiers(String tierName, boolean unlimitedEnergy, MMLong maxEnergy, MMLong defaultEnergy, boolean unlimitedInput, MMLong maxInput, boolean unlimitedExtract, MMLong maxExtract, int textureColor) {
        this.tierName = tierName;
        this.unlimitedEnergy = unlimitedEnergy;
        this.maxEnergy = maxEnergy.copy();
        this.defaultEnergy = defaultEnergy.copy();
        this.unlimitedInput = unlimitedInput;
        this.maxInput = maxInput.copy();
        this.unlimitedExtract = unlimitedExtract;
        this.maxExtract = maxExtract.copy();
        this.textureColor = textureColor;
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
    
    @Override
    public int getTextureColor() {
        return textureColor;
    }
    
    
}
