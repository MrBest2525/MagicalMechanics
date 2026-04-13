package com.github.mrbest2525.magicalmechanics.datagen.client.item.model.item.machienpart;

import com.github.mrbest2525.magicalmechanics.MagicalMechanics;
import com.github.mrbest2525.magicalmechanics.content.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;

public class SidePartItemModelGenerator extends IMachineFramePartModelGenerator {
    
    private final ItemModelProvider provider;
    
    public SidePartItemModelGenerator(ItemModelProvider provider) {
        this.provider = provider;
    }
    
    @Override
    public void generateModel() {
        provider.withExistingParent(ModItems.UNLIMITED_WIRELESS_ENERGY_INTERFACE.getRegisteredName(), MachinePartStaticPaths.ModelRLs.WIRELESS_ENERGY_INTERFACE)
                .texture("all", ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.SIDE + "/wireless_energy_interface/unlimited_wireless_energy_interface"));
    }
}
