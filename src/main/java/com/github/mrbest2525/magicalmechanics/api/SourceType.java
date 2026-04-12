package com.github.mrbest2525.magicalmechanics.api;

public enum SourceType {
    // 電力
    MagicalFlux("magical_flux", 0xFF4CC9F0),
    // 機械での魔力
    ViservalFlux("viserval_flux", 0xFFB5179E),
    // Playerや自然界の魔力
    Mana("mana", 0xFF4361EE),
    // 熱
    Thermal("thermal", 0xFFF72585);
    
    // StringRepresentable のための実装
    private final String name;
    private final int color;
    
    SourceType(String name, int color) {
        this.name = name;
        this.color = color;
    }
    
    
    public String getName() {
        return this.name;
    }
    
    public int getColor() {
        return this.color;
    }
}
