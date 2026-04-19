package io.github.mrbest2525.magicalmechanics.datagen.client.item.model.item.machienpart;

import io.github.mrbest2525.magicalmechanics.MagicalMechanics;
import io.github.mrbest2525.magicalmechanics.content.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;

public class CorePartItemModelGenerator extends IMachineFramePartModelGenerator{
    private final ItemModelProvider provider;
    
    public CorePartItemModelGenerator(ItemModelProvider provider) {
        this.provider = provider;
    }
    
    @Override
    public void generateModel() {
        // FurnaceCore
        provider.withExistingParent(ModItems.FURNACE_CORE.getRegisteredName(), MachinePartStaticPaths.ModelRLs.CORE_PART_ALL)
                .texture("all", ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/furnace_core/furnace_core"));
        
        // EnergyCores
        provider.withExistingParent(ModItems.UNLIMITED_ENERGY_CORE.getRegisteredName(), MachinePartStaticPaths.ModelRLs.CORE_PART_MASK)
                .texture("up",    ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_top"))
                .texture("down",  ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_top"))
                .texture("north", ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side"))
                .texture("south", ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side"))
                .texture("east",  ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side"))
                .texture("west",  ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side"))
                .texture("mask_up",    ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_top_mask"))
                .texture("mask_down",  ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_top_mask"))
                .texture("mask_north", ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side_mask"))
                .texture("mask_south", ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side_mask"))
                .texture("mask_east",  ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side_mask"))
                .texture("mask_west",  ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side_mask"));
        
        provider.withExistingParent(ModItems.NORMAL_ENERGY_CORE.getRegisteredName(), MachinePartStaticPaths.ModelRLs.CORE_PART_MASK)
                .texture("up",    ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_top"))
                .texture("down",  ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_top"))
                .texture("north", ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side"))
                .texture("south", ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side"))
                .texture("east",  ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side"))
                .texture("west",  ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side"))
                .texture("mask_up",    ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_top_mask"))
                .texture("mask_down",  ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_top_mask"))
                .texture("mask_north", ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side_mask"))
                .texture("mask_south", ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side_mask"))
                .texture("mask_east",  ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side_mask"))
                .texture("mask_west",  ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side_mask"));
        
        provider.withExistingParent(ModItems.ADVANCED_ENERGY_CORE.getRegisteredName(), MachinePartStaticPaths.ModelRLs.CORE_PART_MASK)
                .texture("up",    ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_top"))
                .texture("down",  ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_top"))
                .texture("north", ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side"))
                .texture("south", ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side"))
                .texture("east",  ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side"))
                .texture("west",  ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side"))
                .texture("mask_up",    ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_top_mask"))
                .texture("mask_down",  ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_top_mask"))
                .texture("mask_north", ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side_mask"))
                .texture("mask_south", ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side_mask"))
                .texture("mask_east",  ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side_mask"))
                .texture("mask_west",  ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, MachinePartStaticPaths.TextureFilePaths.CORE + "/magical_flux_core/magical_flux_core_side_mask"));
    }
}
