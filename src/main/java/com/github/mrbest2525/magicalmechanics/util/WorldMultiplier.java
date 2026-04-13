package com.github.mrbest2525.magicalmechanics.util;

import java.util.Random;

public class WorldMultiplier {
    // TODO 倍率メゾッドと倍率の読み込みなど
    
    public WorldMultiplier() {
    
    }
    
    public void initializeRandomly(long seed) {
        Random rand = new Random(seed);
        
        productionWorldEnergyMultiplier = 0.9f + rand.nextFloat() * 0.2f;
        consumptionWorldEnergyMultiplier = 0.9f + rand.nextFloat() * 0.2f;
        
        productionWorldMagicFluxMultiplier = 0.9f + rand.nextFloat() * 0.2f;
        consumptionWorldMagicFluxMultiplier = 0.9f + rand.nextFloat() * 0.2f;
        
        productionWorldManaMultiplier = 0.9f + rand.nextFloat() * 0.2f;
        consumptionWorldManaMultiplier = 0.9f + rand.nextFloat() * 0.2f;
        
        productionWorldItemMultiplier = 0.9f + rand.nextFloat() * 0.2f;
        consumptionWorldItemMultiplier = 0.9f + rand.nextFloat() * 0.2f;
    }
    
    public float productionEnergyMultiplier = 1;
    public float productionWorldEnergyMultiplier = 1;
    public float consumptionEnergyMultiplier = 1;
    public float consumptionWorldEnergyMultiplier = 1;
    
    public float productionMagicFluxMultiplier = 1;
    public float productionWorldMagicFluxMultiplier = 1;
    public float consumptionMagicFluxMultiplier = 1;
    public float consumptionWorldMagicFluxMultiplier = 1;
    
    public float productionManaMultiplier = 1;
    public float productionWorldManaMultiplier = 1;
    public float consumptionManaMultiplier = 1;
    public float consumptionWorldManaMultiplier = 1;
    
    public float productionItemMultiplier = 1;
    public float productionWorldItemMultiplier = 1;
    public float consumptionItemMultiplier = 1;
    public float consumptionWorldItemMultiplier = 1;
    
    // Energy
    public int getProductionEnergy(int base) {return (int) (base * productionEnergyMultiplier);}
    public int getProductionWorldEnergy(int base) {return (int) (base * productionWorldEnergyMultiplier);}
    public int getConsumptionEnergy(int base) {return (int) (base * consumptionEnergyMultiplier);}
    public int getConsumptionWorldEnergy(int base) {return (int) (base * consumptionWorldEnergyMultiplier);}
    
    // MagicFlux
    public int getProductionMagicFlux(int base) {return (int) (base * productionMagicFluxMultiplier);}
    public int getProductionWorldMagicFlux(int base) {return (int) (base * productionWorldMagicFluxMultiplier);}
    public int getConsumptionMagicFlux(int base) {return (int) (base * consumptionMagicFluxMultiplier);}
    public int getConsumptionWorldMagicFlux(int base) {return (int) (base * consumptionWorldMagicFluxMultiplier);}

    // Mana
    public int getProductionMana(int base) {return (int) (base * productionManaMultiplier);}
    public int getProductionWorldMana(int base) {return (int) (base * productionWorldManaMultiplier);}
    public int getConsumptionMana(int base) {return (int) (base * consumptionManaMultiplier);}
    public int getConsumptionWorldMana(int base) {return (int) (base * consumptionWorldManaMultiplier);}
    
    // Item
    public int getProductionItem(int base) {return (int) (base * productionItemMultiplier);}
    public int getProductionWorldItem(int base) {return (int) (base * productionWorldItemMultiplier);}
    public int getConsumptionItem(int base) {return (int) (base * consumptionItemMultiplier);}
    public int getConsumptionWorldItem(int base) {return (int) (base * consumptionWorldItemMultiplier);}
}
