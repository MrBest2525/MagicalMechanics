package io.github.mrbest2525.magicalmechanics.datagen.client.item;

import io.github.mrbest2525.magicalmechanics.MagicalMechanics;
import io.github.mrbest2525.magicalmechanics.content.block.ModBlockItems;
import io.github.mrbest2525.magicalmechanics.content.item.ModItems;
import io.github.mrbest2525.magicalmechanics.datagen.client.item.model.blockitem.MachineFrameBlockItemModelGenerator;
import io.github.mrbest2525.magicalmechanics.datagen.client.item.model.item.MayurantItemModelGenerator;
import io.github.mrbest2525.magicalmechanics.datagen.client.item.model.item.machienpart.MachineFramePartModels;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MagicalMechanics.MODID, existingFileHelper);
    }
    
    @Override
    protected void registerModels() {
        // mayurant
        MayurantItemModelGenerator mayurantGen = new MayurantItemModelGenerator(this);
        for (DeferredItem<Item> mayurantItem: ModItems.MAYURANT_ITEMS.values()) {
            mayurantGen.generateMayurantModel(mayurantItem);
        }
        // MachineFrameBlockItem
        MachineFrameBlockItemModelGenerator machineFrameBlockGen = new MachineFrameBlockItemModelGenerator(this);
        for (DeferredItem<BlockItem> blockItem: ModBlockItems.MACHINE_FRAME_BLOCK_ITEMS.values()) {
            machineFrameBlockGen.generateMachineFrameBlockItem(blockItem);
        }
        
        // MachineFramePartItems
        MachineFramePartModels machineFramePartModels = new MachineFramePartModels(this);
        machineFramePartModels.registerModels();
    }
}
