package io.github.mrbest2525.magicalmechanics.datagen.client.item.model.item.machienpart;

import io.github.mrbest2525.magicalmechanics.MagicalMechanics;
import net.minecraft.resources.ResourceLocation;

public class MachinePartStaticPaths {
    
    public static class ModelRLs {
        public static final ResourceLocation ITEM = ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "item");
        
        public static final ResourceLocation CORE_PART = ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, ModelRLs.ITEM.getPath() + "/core_part");
        public static final ResourceLocation CORE_PART_ALL = ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, ModelRLs.ITEM.getPath() + "/core_part_all");
        public static final ResourceLocation CORE_PART_MASK = ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, ModelRLs.ITEM.getPath() + "/core_part_mask");
        
        public static final ResourceLocation WIRELESS_ENERGY_INTERFACE = ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, ModelRLs.ITEM.getPath() + "/wireless_energy_interface");
    }
    
    public static class TextureFilePaths {
        public static final String ITEM = "item";
        public static final String MACHINE_PART = ITEM + "/machine_part";
        public static final String CORE = MACHINE_PART + "/core";
        public static final String SIDE = MACHINE_PART + "/side";
    }
}
