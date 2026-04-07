package com.github.mrbest2525.magicalmechanics.datagen.client.item.model.item.machienpart;

import net.neoforged.neoforge.client.model.generators.ItemModelProvider;

import java.util.ArrayList;
import java.util.List;

public class MachineFramePartModels {
    private final ItemModelProvider provider;
    
    public MachineFramePartModels(ItemModelProvider provider) {
        this.provider = provider;
    }
    
    public void registerModels() {
        List<IMachineFramePartModelGenerator> list = new ArrayList<>();
        list.add(new CorePartItemModelGenerator(provider));
        list.add(new SidePartItemModelGenerator(provider));
        
        list.forEach(IMachineFramePartModelGenerator::generateModel);
    }
}
